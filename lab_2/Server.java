package lab_2;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Server {
    public static void main(String[] argv) throws Exception {
        String clientSentence = "";
        String outToClientSentence = "";
        ServerSocket welcomeSocket = null;
        DataInputStream inFromClient = null;
        DataOutputStream outToClient = null;
        byte[] answer = new byte[100];
        try {
            welcomeSocket = new ServerSocket();

        } catch (Exception e) {
            System.out.println("Can't initialize server");
        }
        assert welcomeSocket != null;
        Socket connectionSocket = null;
        try {
            welcomeSocket.bind(new InetSocketAddress(7));
        } catch (Exception e) {
            System.out.println("Can't bind server socket");
        }
        while (true) {
            connectionSocket = welcomeSocket.accept();
            while (true) {
                try {
                    assert connectionSocket != null;
                    inFromClient = new DataInputStream(connectionSocket.getInputStream());
                    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                } catch (Exception e) {
                    break;
                }
                int answerSize = inFromClient.read(answer);
                if (answerSize == -1) break;
                System.out.println("Message: " + new String(answer,0,answerSize));
                System.out.println("Size: " + answerSize);
                outToClient.write(answer, 0, answerSize);
            }
        }
    }
}
