import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultProcessor {
    static class Result {
        final String file;
        final boolean success;
        final String message;
        Result(String file, boolean success, String message) {
            this.file = file; this.success = success; this.message = message;
        }
    }

    static class ProcessFile implements Callable<Result> {
        private final Path path;
        ProcessFile(Path path) { this.path = path; }

        public Result call() {
            try {
                String text = Files.readString(path).trim();
                if (text.isEmpty()) throw new IOException("Empty score file");
                int score = Integer.parseInt(text);
                if (score < 0 || score > 100) throw new IllegalArgumentException("Score outside 0..100");
                String status = score >= 40 ? "PASS" : "FAIL";
                return new Result(path.getFileName().toString(), true, status + " (" + score + ")");
            } catch (Exception e) {
                return new Result(path.getFileName().toString(), false, e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Path inputDir = Paths.get(args.length > 0 ? args[0] : "input");
        int poolSize = args.length > 1 ? Integer.parseInt(args[1]) : 4;
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        ConcurrentLinkedQueue<Result> results = new ConcurrentLinkedQueue<>();
        long start = System.nanoTime();

        List<Future<Result>> futures = new ArrayList<>();
        try {
            if (!Files.isDirectory(inputDir)) {
                System.out.println("Input directory not found: " + inputDir.toAbsolutePath());
                return;
            }
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, "*.txt")) {
                for (Path path : stream) futures.add(pool.submit(new ProcessFile(path)));
            }
            for (Future<Result> future : futures) results.add(future.get());
        } finally {
            pool.shutdown();
            if (!pool.awaitTermination(1, TimeUnit.MINUTES)) pool.shutdownNow();
        }

        int success = 0, failure = 0;
        for (Result r : results) {
            if (r.success) success++; else failure++;
            System.out.println(r.file + " -> " + (r.success ? r.message : "ERROR: " + r.message));
        }
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        System.out.println("Summary: success=" + success + ", failure=" + failure);
        System.out.println("Pool size=" + poolSize + ", elapsed_ms=" + elapsedMs);
    }
}
