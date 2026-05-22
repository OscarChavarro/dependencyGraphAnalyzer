package graphbuilderplugins.typescriptsources.runner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class TypeScriptAnalyzerRunner {
    private static final String SCRIPT_RESOURCE_PATH = "/typescript-analyzer/analyze-typescript-dependencies.js";
    private static final Path SCRIPT_SOURCE_PATH_A = Path.of(
            "src", "main", "resources", "typescript-analyzer", "analyze-typescript-dependencies.js");
    private static final Path SCRIPT_SOURCE_PATH_B = Path.of(
            "graphBuilderPlugins", "src", "main", "resources", "typescript-analyzer", "analyze-typescript-dependencies.js");

    public String run(
            List<Path> inputFolders,
            List<String> excludedFolders,
            List<String> excludedSuffixes,
            List<String> includedExtensions) {
        if (inputFolders == null || inputFolders.isEmpty()) {
            return "{\"nodes\":[],\"edges\":[],\"badNodes\":[]}";
        }

        Path scriptFile = resolveScriptPath();
        boolean deleteAfterRun = !isLocalScript(scriptFile);
        String payload = buildPayload(inputFolders, excludedFolders, excludedSuffixes, includedExtensions);
        ProcessBuilder builder = new ProcessBuilder("node", scriptFile.toString());
        builder.redirectErrorStream(false);

        try {
            Process process = builder.start();
            process.getOutputStream().write(payload.getBytes(StandardCharsets.UTF_8));
            process.getOutputStream().close();

            String stdout = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            String stderr = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8).trim();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IllegalStateException(
                        "TypeScript analyzer failed with exit code " + exitCode + ". stderr: " + stderr);
            }
            if (stdout.isBlank()) {
                throw new IllegalStateException("TypeScript analyzer returned empty JSON output.");
            }
            return stdout;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start TypeScript analyzer process (node).", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("TypeScript analyzer interrupted.", e);
        } finally {
            if (deleteAfterRun) {
                try {
                    Files.deleteIfExists(scriptFile);
                } catch (IOException ignored) {
                    // noop
                }
            }
        }
    }

    private Path resolveScriptPath() {
        for (Path candidate : List.of(SCRIPT_SOURCE_PATH_A, SCRIPT_SOURCE_PATH_B)) {
            Path localScript = candidate.toAbsolutePath().normalize();
            if (Files.isRegularFile(localScript)) {
                return localScript;
            }
        }
        return extractScriptToTempFile();
    }

    private boolean isLocalScript(Path scriptPath) {
        Path normalized = scriptPath.toAbsolutePath().normalize();
        return normalized.equals(SCRIPT_SOURCE_PATH_A.toAbsolutePath().normalize())
                || normalized.equals(SCRIPT_SOURCE_PATH_B.toAbsolutePath().normalize());
    }

    private Path extractScriptToTempFile() {
        try (InputStream inputStream = TypeScriptAnalyzerRunner.class.getResourceAsStream(SCRIPT_RESOURCE_PATH)) {
            if (inputStream == null) {
                throw new IllegalStateException("TypeScript analyzer script resource not found: " + SCRIPT_RESOURCE_PATH);
            }
            Path tempFile = Files.createTempFile("typescript-analyzer-", ".js");
            Files.writeString(tempFile, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
            tempFile.toFile().deleteOnExit();
            return tempFile;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to prepare TypeScript analyzer script.", e);
        }
    }

    private String buildPayload(
            List<Path> inputFolders,
            List<String> excludedFolders,
            List<String> excludedSuffixes,
            List<String> includedExtensions) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"inputFolders\":").append(toJsonArray(inputFolders.stream().map(Path::toString).toList())).append(",");
        json.append("\"excludedFolders\":").append(toJsonArray(excludedFolders)).append(",");
        json.append("\"excludedSuffixes\":").append(toJsonArray(excludedSuffixes)).append(",");
        json.append("\"includedExtensions\":").append(toJsonArray(includedExtensions));
        json.append("}");
        return json.toString();
    }

    private String toJsonArray(List<String> values) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        boolean first = true;
        for (String value : values) {
            if (!first) {
                json.append(",");
            }
            first = false;
            json.append("\"").append(escapeJson(value)).append("\"");
        }
        json.append("]");
        return json.toString();
    }

    private String escapeJson(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

}
