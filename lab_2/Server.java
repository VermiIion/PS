package lab_2;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] argv) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        try {
            ServerSocket welcomeSocket = new ServerSocket(6789);
        }catch (Exception e){
            System.out.println("Can't initialize server");
        }
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            capitalizedSentence = clientSentence.toUpperCase() + 'n';
            outToClient.writeBytes(capitalizedSentence);
        }
    }
}
