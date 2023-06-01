package TCP_ECHO;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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
        port = inputPort(port, validatePort(port));
        try {
            clientSocket = new Socket(ip, port);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
        } catch (Exception e) {
            System.out.println("Can't connect to server");
            return;
        }
        System.out.println("Connection Succesfull!");
        do {
            System.out.print("Message to server: ");
            sentence = inFromUser.readLine();
            outToServer.write(sentence.getBytes());
            System.out.println("Bytes sent: " + sentence.length());
            int answer_size = inFromServer.read(answer);
            modifiedSentence = new String(answer, 0, answer_size);
            System.out.println("FROM SERVER: " + modifiedSentence + "\nBytes received: " + answer_size);

        } while (!sentence.isEmpty());
        clientSocket.close();
    }

    static int inputPort(int port, boolean b) {
        String sentence;
        Scanner input = new Scanner(System.in);
        while(true) {
            System.out.println("Current port number: " + port);
            System.out.print("Do you wish to change default port settings?(Y/N): ");
            sentence = input.next();
            if(!sentence.equals("Y") && !sentence.equals("N")) System.out.println("Incorrect value");
            else if(sentence.equals("Y")){
                while(true){
                    System.out.print("Input new port value: ");
                    port = input.nextInt();
                    if(b) break;
                    System.out.println("Incorrect port number");
                }
            }
            else break;
        }
        return port;
    }

    public static boolean validatePort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
