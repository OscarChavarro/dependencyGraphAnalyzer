package backend.application.service;

import backend.application.port.in.DownloadLfsSourceUseCase;
import backend.domain.model.AptSourceLocationModel;
import backend.domain.model.LfsSourceDownloadResultModel;
import backend.domain.model.LfsSourceDownloadStatus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Runs "apt-get source PACKAGE" inside the project's linuxFromScratch folder (created when
 * missing), unless the source files and the unpacked directory are already there. The full
 * output is appended to apt-get-source.log in that folder; on failure the tail of the output
 * is returned as message.
 */
@Service
public class DownloadLfsSourceService implements DownloadLfsSourceUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DownloadLfsSourceService.class);
    static final Pattern APT_PACKAGE_NAME = Pattern.compile("^[a-z0-9][a-z0-9+.\\-]*$");
    private static final String LOG_FILE_NAME = "apt-get-source.log";
    private static final long TIMEOUT_MINUTES = 30;
    private static final int ERROR_TAIL_LINES = 6;

    private final LfsFolderResolver lfsFolderResolver;
    private final AptSourceLocator aptSourceLocator;

    public DownloadLfsSourceService(LfsFolderResolver lfsFolderResolver, AptSourceLocator aptSourceLocator) {
        this.lfsFolderResolver = lfsFolderResolver;
        this.aptSourceLocator = aptSourceLocator;
    }

    @Override
    public LfsSourceDownloadResultModel execute(String projectId, String packageName, int position, int total) {
        String name = packageName == null ? "" : packageName.trim();
        if (!APT_PACKAGE_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("Invalid apt package name: '" + packageName + "'");
        }
        Path folder = lfsFolderResolver.resolveAndCreate(projectId);
        String progress = progressLabel(position, total);
        long startNanos = System.nanoTime();

        Optional<AptSourceLocationModel> location = aptSourceLocator.locate(folder, name);
        if (location.isPresent() && isAlreadyPresent(folder, location.get())) {
            LOG.info("LFS {} {} skipped: source {} already present in {}",
                    progress, name, location.get().directoryName(), folder);
            return LfsSourceDownloadResultModel.succeeded(name, LfsSourceDownloadStatus.ALREADY_PRESENT, location.get());
        }

        LOG.info("LFS {} apt-get source {} (folder={})", progress, name, folder);
        Deque<String> tail = new ArrayDeque<>();
        StringBuilder fullOutput = new StringBuilder();
        int exitCode;
        try {
            Process process = new ProcessBuilder(List.of("apt-get", "source", name))
                    .directory(folder.toFile())
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    LOG.debug("LFS {} {}: {}", progress, name, line);
                    fullOutput.append(line).append(System.lineSeparator());
                    tail.addLast(line);
                    if (tail.size() > ERROR_TAIL_LINES) {
                        tail.removeFirst();
                    }
                }
            }
            if (!process.waitFor(TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                process.destroyForcibly();
                appendLog(folder, name, fullOutput + "TIMEOUT after " + TIMEOUT_MINUTES + " minutes");
                LOG.warn("LFS {} apt-get source {} FAILED: timeout after {} minutes", progress, name, TIMEOUT_MINUTES);
                return LfsSourceDownloadResultModel.failed(name, -1, "timeout after " + TIMEOUT_MINUTES + " minutes");
            }
            exitCode = process.exitValue();
        } catch (IOException e) {
            LOG.warn("LFS {} apt-get source {} failed to start. folder={}", progress, name, folder, e);
            return LfsSourceDownloadResultModel.failed(name, -1, "failed to run apt-get: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return LfsSourceDownloadResultModel.failed(name, -1, "interrupted");
        }

        appendLog(folder, name, fullOutput + "exit code: " + exitCode);
        if (exitCode != 0) {
            LOG.warn("LFS {} apt-get source {} FAILED: exitCode={}, seconds={}, lastLine={}",
                    progress, name, exitCode, elapsedSeconds(startNanos), tail.peekLast());
            return LfsSourceDownloadResultModel.failed(name, exitCode, String.join("\n", tail));
        }
        LOG.info("LFS {} apt-get source {} OK, seconds={}", progress, name, elapsedSeconds(startNanos));
        AptSourceLocationModel resolved = location.orElseGet(() -> aptSourceLocator.locate(folder, name).orElse(null));
        return LfsSourceDownloadResultModel.succeeded(name, LfsSourceDownloadStatus.DOWNLOADED, resolved);
    }

    private static boolean isAlreadyPresent(Path folder, AptSourceLocationModel location) {
        return location.allFilesDownloaded() && Files.isDirectory(folder.resolve(location.directoryName()));
    }

    private static String progressLabel(int position, int total) {
        if (position <= 0) {
            return "[?]";
        }
        return total > 0 ? "[" + position + "/" + total + "]" : "[" + position + "]";
    }

    private static long elapsedSeconds(long startNanos) {
        return TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startNanos);
    }

    private void appendLog(Path folder, String packageName, String output) {
        String entry = "===== " + LocalDateTime.now() + " apt-get source " + packageName + System.lineSeparator()
                + output + System.lineSeparator();
        try {
            Files.writeString(folder.resolve(LOG_FILE_NAME), entry, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.warn("Cannot write {} in {}", LOG_FILE_NAME, folder, e);
        }
    }
}
