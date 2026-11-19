import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PlaylistRedesignBenchmark {
    private static volatile int sink;

    private static long arrayListUpNext(int n) {
        List<Integer> list = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) list.add(0, i);
        for (int i = 0; i < n; i++) sink += list.remove(0);
        return System.nanoTime() - start;
    }

    private static long arrayDequeUpNext(int n) {
        Deque<Integer> deque = new ArrayDeque<>();
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) deque.addFirst(i);
        for (int i = 0; i < n; i++) sink += deque.removeFirst();
        return System.nanoTime() - start;
    }

    public static void main(String[] args) {
        int[] sizes = {1000, 5000, 10000};
        System.out.println("n,arraylist_front_add_remove_ns,arraydeque_front_add_remove_ns");
        for (int n : sizes) {
            long original = arrayListUpNext(n);
            long redesigned = arrayDequeUpNext(n);
            System.out.println(n + "," + original + "," + redesigned);
        }
        System.out.println("sink=" + sink);
        System.out.println("Use your own repeated runs as final evidence; timings vary by machine.");
    }
}
