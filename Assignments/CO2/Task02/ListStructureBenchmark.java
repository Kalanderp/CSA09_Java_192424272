import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ListStructureBenchmark {
    private static volatile int sink;

    private static long timeAddEnd(List<Integer> list, int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(i);
        return System.nanoTime() - start;
    }

    private static long timeFrontInsert(List<Integer> list, int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(0, i);
        return System.nanoTime() - start;
    }

    private static long timeMiddleInsert(List<Integer> list, int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(list.size() / 2, i);
        return System.nanoTime() - start;
    }

    private static long timeGet(List<Integer> list, int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) sink += list.get((i * 31) % list.size());
        return System.nanoTime() - start;
    }

    private static long timeIterate(List<Integer> list) {
        long start = System.nanoTime();
        for (int value : list) sink += value;
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000};
        System.out.println("structure,size,add_end_ns,front_insert_ns,middle_insert_ns,random_get_10000_ns,iterate_ns");
        for (int n : sizes) {
            List<Integer> a = new ArrayList<>();
            List<Integer> l = new LinkedList<>();
            long ae = timeAddEnd(a, n), le = timeAddEnd(l, n);
            long af = timeFrontInsert(new ArrayList<>(), n);
            long lf = timeFrontInsert(new LinkedList<>(), n);
            long am = timeMiddleInsert(new ArrayList<>(), n);
            long lm = timeMiddleInsert(new LinkedList<>(), n);
            long ag = timeGet(a, 10000), lg = timeGet(l, 10000);
            long ai = timeIterate(a), li = timeIterate(l);
            System.out.printf(Locale.US, "ArrayList,%d,%d,%d,%d,%d,%d%n", n, ae, af, am, ag, ai);
            System.out.printf(Locale.US, "LinkedList,%d,%d,%d,%d,%d,%d%n", n, le, lf, lm, lg, li);
            // ArrayDeque is included for the changed Up Next requirement (both-end operations).
            ArrayDeque<Integer> deque = new ArrayDeque<>();
            long ds = System.nanoTime();
            for (int i = 0; i < n; i++) { deque.addFirst(i); deque.addLast(i); deque.removeFirst(); }
            long dequeOps = System.nanoTime() - ds;
            System.out.printf(Locale.US, "ArrayDeque,%d,,,,,%d%n", n, dequeOps);
        }
        System.out.println("sink=" + sink);
        System.out.println("Run more than once; compare trends, not one timing.");
    }
}
