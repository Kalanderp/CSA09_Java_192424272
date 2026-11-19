import java.io.*;
import java.util.*;

public class TryWithResourcesReport {
    public static void main(String[] args) {
        List<String> inputFiles = Arrays.asList("store1.txt", "store2.txt", "store3.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("summary-twr.txt"))) {
            for (String name : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            System.out.println("Report generated: summary-twr.txt");
        } catch (IOException e) {
            System.err.println("Report failed: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.err.println("Suppressed: " + suppressed);
            }
        }
    }
}
