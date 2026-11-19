import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PharmacyInventory {
    private static final class Medicine {
        final String name;
        int quantity;
        Medicine(String name, int quantity) { this.name = name; this.quantity = quantity; }
        public String toString() { return name + "(qty=" + quantity + ")"; }
    }

    private static Map<String, Medicine> createMap(int type) {
        if (type == 0) return new HashMap<>();
        if (type == 1) return new TreeMap<>();
        return new LinkedHashMap<>();
    }

    private static long lookupTime(Map<String, Medicine> map, String key) {
        long start = System.nanoTime();
        for (int i = 0; i < 100000; i++) map.get(key);
        return System.nanoTime() - start;
    }

    private static long iterationTime(Map<String, Medicine> map) {
        long start = System.nanoTime();
        long count = 0;
        for (Map.Entry<String, Medicine> entry : map.entrySet()) count += entry.getValue().quantity;
        if (count == -1) System.out.println(count);
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 10000, 100000};
        System.out.println("size,map,lookup_100000_ns,iteration_report_ns");
        for (int size : sizes) {
            @SuppressWarnings("unchecked")
            Map<String, Medicine>[] maps = new Map[] {
                createMap(0), createMap(1), createMap(2)
            };
            for (int i = 0; i < size; i++) {
                String code = String.format("MED%06d", i);
                Medicine medicine = new Medicine("Medicine" + i, (i % 200) + 1);
                for (Map<String, Medicine> map : maps) map.put(code, medicine);
            }
            String target = String.format("MED%06d", size / 2);
            String[] names = {"HashMap", "TreeMap", "LinkedHashMap"};
            for (int i = 0; i < maps.length; i++) {
                long lookup = lookupTime(maps[i], target);
                long report = iterationTime(maps[i]);
                System.out.println(size + "," + names[i] + "," + lookup + "," + report);
            }
            if (size == 1000) {
                System.out.println("Sample code-order report (TreeMap): " + maps[1].keySet().stream().limit(5).toList());
                System.out.println("Sample insertion-order report (LinkedHashMap): " + maps[2].keySet().stream().limit(5).toList());
            }
        }
    }
}
