package recommendation.server;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReader {
    private final BufferedReader reader;

    public InputReader(BufferedReader reader) {
        this.reader = reader;
    }

    public String readString(String prompt) throws IOException {
        System.out.print(prompt);
        return reader.readLine();
    }

    public int readInt(String prompt) throws IOException {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public double readDouble(String prompt) throws IOException {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid double.");
            }
        }
    }
}
