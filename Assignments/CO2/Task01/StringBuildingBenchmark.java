import java.util.Locale;

public class StringBuildingBenchmark {
    private static volatile int blackHole;

    private interface Builder {
        String build(String[] fields);
    }

    private static String usingPlus(String[] fields) {
        String result = "";
        for (String field : fields) result = result + field;
        return result;
    }

    private static String usingStringBuilder(String[] fields) {
        StringBuilder result = new StringBuilder();
        for (String field : fields) result.append(field);
        return result.toString();
    }

    private static String usingStringBuffer(String[] fields) {
        StringBuffer result = new StringBuffer();
        for (String field : fields) result.append(field);
        return result.toString();
    }

    private static long measure(Builder builder, String[] fields, int repetitions) {
        // Warm-up reduces (but does not remove) JVM compilation effects.
        for (int i = 0; i < Math.min(1000, repetitions); i++) blackHole ^= builder.build(fields).length();
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) blackHole ^= builder.build(fields).length();
        return (System.nanoTime() - start) / repetitions;
    }

    private static String[] fields(int count) {
        String[] values = new String[count];
        for (int i = 0; i < count; i++) values[i] = "FIELD" + i + "|";
        return values;
    }

    public static void main(String[] args) {
        int[][] workloads = {{6, 1000}, {50, 10000}, {200, 3000}};
        System.out.println("fields,tickets,plus_ns_per_ticket,builder_ns_per_ticket,buffer_ns_per_ticket");
        for (int[] workload : workloads) {
            String[] data = fields(workload[0]);
            int tickets = workload[1];
            long plus = measure(StringBuildingBenchmark::usingPlus, data, tickets);
            long builder = measure(StringBuildingBenchmark::usingStringBuilder, data, tickets);
            long buffer = measure(StringBuildingBenchmark::usingStringBuffer, data, tickets);
            System.out.printf(Locale.US, "%d,%d,%d,%d,%d%n",
                    workload[0], tickets, plus, builder, buffer);
        }
        System.out.println("blackHole=" + blackHole);
        System.out.println("Note: timings are machine/JVM dependent; run repeatedly on your own laptop.");
    }
}
