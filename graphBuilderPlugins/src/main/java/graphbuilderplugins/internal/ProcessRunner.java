package graphbuilderplugins.internal;

import java.io.IOException;

public final class ProcessRunner {
    private ProcessRunner() {
    }

    public static Process start(String... args) {
        try {
            return new ProcessBuilder(args).start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start process: " + String.join(" ", args), e);
        }
    }

    public static void waitFor(Process process) {
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Process wait interrupted", e);
        }
    }
}
