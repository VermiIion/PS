package TCP_ECHO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client4 {
    public static void main(String[] argv) throws Exception {
        String sentence = "";
        String modifiedSentence = "";
        byte[] answer = new byte[100];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = null;
        DataOutputStream outToServer = null;
        DataInputStream inFromServer = null;
        Client.ConnectionHandler(answer, inFromUser);
    }
}
