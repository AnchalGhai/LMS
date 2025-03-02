import java.io.*;
import java.util.*;

public class ActivityFile {
    private String fileName;

    // Constructor
    public ActivityFile(String fileName) {
        this.fileName = fileName;
    }

    // Write a line to the activity log file
    public void writeLine(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(line);
            writer.newLine();  // New line after each activity
        } catch (IOException e) {
            System.out.println("Error writing to activity log file: " + e.getMessage());
        }
    }

    // Read all lines from the activity log file
    public List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading from activity log file: " + e.getMessage());
        }
        return lines;
    }
}
