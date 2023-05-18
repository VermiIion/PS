package TCP_ECHO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Client {
    public static void main(String[] argv) throws Exception {
        byte[] answer = new byte[100];
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        ConnectionHandler(answer, inFromUser);
    }

    public static void ConnectionHandler(byte[] answer, BufferedReader inFromUser) throws IOException {
        Socket clientSocket;
        DataOutputStream outToServer;
        DataInputStream inFromServer;
        String sentence;
        String modifiedSentence;
        int port = 7;
        String ip = "localhost";
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("Do you wish to change default IP/port settings?(Y/N)");
            sentence = input.next();
            if(!sentence.equals("Y") && !sentence.equals("N")) System.out.println("Incorrect value");
            else if(sentence.equals("Y")){
                System.out.println("IP: ");
                ip = input.next();
            }
            else break;
        }
        try {
            clientSocket = new Socket(ip, port);
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
