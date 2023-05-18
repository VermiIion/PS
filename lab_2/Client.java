package lab_2;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] argv) throws Exception {
        String sentence = "";
        String modifiedSentence = "";
        byte[] answer = new byte[100];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = null;
        DataOutputStream outToServer = null;
        DataInputStream inFromServer = null;
        TCP_ECHO.Client.ConnectionHandler(answer, inFromUser);
    }
}
