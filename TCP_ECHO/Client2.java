package TCP_ECHO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client2 {
    public static void main(String[] argv) throws Exception {
        String sentence = "";
        String modifiedSentence = "";
        byte[] answer = new byte[100];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        Socket clientSocket = null;
        DataOutputStream outToServer = null;
        DataInputStream inFromServer = null;
        try {
            clientSocket = new Socket("localhost", 7);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            System.out.println("Can't connect to server");
            return;
        }
        do {
            sentence = inFromUser.readLine();
            outToServer.write(sentence.getBytes());
            System.out.println("Bytes sent: " + sentence.length());
            int answer_size = inFromServer.read(answer);
            modifiedSentence = new String(answer, 0, answer_size);
            System.out.println("FROM SERVER: " + modifiedSentence + "\nBytes received: " + answer_size);

        } while (!sentence.isEmpty());
        clientSocket.close();
    }
}
