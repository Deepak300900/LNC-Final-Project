package recommendation.client;

import java.io.*;

public class ServerResponseHandler {
    public static void printServerResponse(BufferedReader in) throws IOException {
        String serverResponse;
        while (!(serverResponse = in.readLine()).equalsIgnoreCase("End of Response")) {
            System.out.println(serverResponse);
        }
    }
}
