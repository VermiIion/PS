package TCP_ECHO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientContainer extends Thread{
    Socket clientSocket;
    int id;
    boolean permission;
    boolean running = false;
    DataInputStream inFromClient = null;
    DataOutputStream outToClient = null;
    byte[] answer = new byte[100];

    public ClientContainer(Socket clientSocket, int id, boolean permission) throws IOException {
        this.clientSocket = clientSocket;
        this.id = id;
        this.permission = permission;
        inFromClient = new DataInputStream(clientSocket.getInputStream());
        outToClient = new DataOutputStream(clientSocket.getOutputStream());
        if(!permission){
            int temp = inFromClient.read(answer);
            answer = "SERVER BUSY".getBytes();
            outToClient.write(answer, 0, answer.length);
            clientSocket.close();
            running = false;
        }
        else {
            running = true;
        }

    }

    @Override
    public void run() {
        if(!permission) return;
        running = true;
        while (true) {
            try {
                int answerSize = inFromClient.read(answer);
                if (answerSize == -1) break;
                System.out.println("#" + id + " Message: " + new String(answer, 0, answerSize));
                System.out.println("Size: " + answerSize);
                outToClient.write(answer, 0, answerSize);
            } catch (Exception e) {
                System.out.println("Connection interrupted");
                break;
            }
        }
        running = false;
    }
}
