package recommendation.server.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Communicationhandler {
    private final BufferedReader reader;
    private final String SUCCESS_MESSAGE = "SUCCESS";
    private final String FAILER_MESSAGE = "FAILED";

    public Communicationhandler(BufferedReader reader) {
        this.reader = reader;
    }

    public String readString(String prompt) throws IOException {
        System.out.print(prompt);
        String input = reader.readLine();
        System.out.println(input);
        return input;
    }

    public int readInt(String prompt) throws IOException {
        System.out.print(prompt);
        Integer input = Integer.parseInt(reader.readLine());
        System.out.println(input);
        return input;
    }

    public double readDouble(String prompt) throws IOException {
        System.out.print(prompt);
        Double input = Double.parseDouble(reader.readLine());
        System.out.println(input);
        return input;
    }

    public String readValidString(String prompt, String errorPrompt, PrintWriter out) throws IOException {
        String input;
        while (true) {
            input = readString(prompt);
            System.out.println(input);
            if (isValidString(input)) {
                out.println(SUCCESS_MESSAGE);
                
                return input;
            }
            printError(errorPrompt, out);
        }
    }

    public double readValidDouble(String prompt, String errorPrompt, PrintWriter out) throws IOException {
        double input;
        while (true) {
            try {
                input = readDouble(prompt);
                System.out.println(input);
                if (isValidString(String.valueOf(input))) {
                    out.println(SUCCESS_MESSAGE);
                    return input;
                }
                printError(errorPrompt, out);
            } catch (NumberFormatException e) {
                printError(errorPrompt, out);
            }
        }
    }

    private boolean isValidString(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private void printError(String errorPrompt, PrintWriter out) {
        out.println(FAILER_MESSAGE);
        out.println(errorPrompt);
        System.out.println(errorPrompt);
        out.flush();
    }

    public void printOperationResult(boolean result, String successMessage, String failureMessage, PrintWriter out) {
        if (result) {
            out.println(successMessage);
            System.out.println(successMessage);
        } else {
            out.println(failureMessage);
            System.out.println(failureMessage);
        }
        out.flush();
    }
}
