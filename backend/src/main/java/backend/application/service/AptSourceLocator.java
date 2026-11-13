package backend.application.service;

import backend.domain.model.AptSourceLocationModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Asks apt which source package provides a binary package and whether its files are already
 * in a folder, by running "apt-get source --print-uris PACKAGE" there (downloads nothing).
 */
@Component
public class AptSourceLocator {
    private static final Logger LOG = LoggerFactory.getLogger(AptSourceLocator.class);
    private static final long TIMEOUT_SECONDS = 120;
    /** 'URI' FILE SIZE HASH: a file apt still has to download. */
    private static final Pattern PENDING_FILE = Pattern.compile("^'[^']+'\\s+(\\S+)\\s+\\d+\\s+\\S+.*$");
    private static final Pattern ALREADY_DOWNLOADED = Pattern.compile("^Skipping already downloaded file '([^']+)'.*$");
    private static final Pattern DSC_FILE = Pattern.compile("^([a-z0-9][a-z0-9+.\\-]*)_([A-Za-z0-9.+~\\-]+)\\.dsc$");

    public Optional<AptSourceLocationModel> locate(Path folder, String packageName) {
        List<String> lines = new ArrayList<>();
        int exitCode;
        try {
            Process process = new ProcessBuilder("apt-get", "source", "--print-uris", packageName)
                    .directory(folder.toFile())
                    .redirectErrorStream(true)
                    .start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }
            if (!process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                LOG.warn("apt-get source --print-uris timed out. package={}", packageName);
                return Optional.empty();
            }
            exitCode = process.exitValue();
        } catch (IOException e) {
            LOG.warn("apt-get source --print-uris failed to start. package={}", packageName, e);
            return Optional.empty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
        if (exitCode != 0) {
            return Optional.empty();
        }
        return parse(lines);
    }

    /** Parses the print-uris output; empty when no .dsc file is mentioned. */
    static Optional<AptSourceLocationModel> parse(List<String> lines) {
        String dscFile = null;
        boolean pendingFiles = false;
        for (String line : lines) {
            String fileName = null;
            Matcher pending = PENDING_FILE.matcher(line.trim());
            Matcher downloaded = ALREADY_DOWNLOADED.matcher(line.trim());
            if (pending.matches()) {
                pendingFiles = true;
                fileName = pending.group(1);
            } else if (downloaded.matches()) {
                fileName = downloaded.group(1);
            }
            if (fileName != null && fileName.endsWith(".dsc")) {
                dscFile = fileName;
            }
        }
        if (dscFile == null) {
            return Optional.empty();
        }
        Matcher dsc = DSC_FILE.matcher(dscFile);
        if (!dsc.matches()) {
            return Optional.empty();
        }
        String sourcePackage = dsc.group(1);
        String version = dsc.group(2);
        return Optional.of(new AptSourceLocationModel(
                sourcePackage, version, sourcePackage + "-" + upstreamVersion(version), !pendingFiles));
    }

    /** Debian version without the "-revision" suffix (the epoch never appears in file names). */
    static String upstreamVersion(String version) {
        int lastHyphen = version.lastIndexOf('-');
        return lastHyphen > 0 ? version.substring(0, lastHyphen) : version;
    }
}
