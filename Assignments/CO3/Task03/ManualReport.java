import java.io.*;
import java.util.*;

public class ManualReport {
    public static void main(String[] args) {
        List<String> inputFiles = Arrays.asList("store1.txt", "store2.txt", "store3.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("summary-manual.txt"));
            for (String name : inputFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Report failed while processing a file: " + e.getMessage());
        } finally {
            if (writer != null) {
                try { writer.close(); }
                catch (IOException e) { System.err.println("Could not close writer: " + e.getMessage()); }
            }
        }
    }
}
