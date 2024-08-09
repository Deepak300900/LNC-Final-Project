package recommendation.client.services;

import java.io.*;

public class ServerResponseService {
    public static void printServerResponse(BufferedReader in) throws IOException {
        String serverResponse = in.readLine();
        while (!serverResponse.equalsIgnoreCase("End of Response") && !serverResponse.equalsIgnoreCase("End of Menu")) {
            System.out.println(serverResponse);
            serverResponse = in.readLine();
        }
    }
}
