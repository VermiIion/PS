package TCP_ECHO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientContainer extends Thread {
    Socket clientSocket;
    ArrayList<ClientContainer> clients;
    int id;
    boolean permission;
    boolean running = true;
    DataInputStream inFromClient;
    DataOutputStream outToClient;
    byte[] answer = new byte[100];

    public ClientContainer(Socket clientSocket, ArrayList<ClientContainer> clients, boolean permission) throws IOException {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.id = clients.size() + 1;
        this.permission = permission;
        inFromClient = new DataInputStream(clientSocket.getInputStream());
        outToClient = new DataOutputStream(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        if (!permission) {
            System.out.println("id: " + id + " Klient nadliczbowy: ");
            try {
                inFromClient.read(answer);
                answer = "SERVER BUSY".getBytes();
                outToClient.write(answer, 0, answer.length);
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Błąd: " + e.getMessage());
            }
            running = false;

        } else {
            System.out.println("id: " + id + " Klient ok: ");
            running = true;
            clients.add(this);
        }
        while (running) {
            try {
                System.out.println("id: " + id + " Czekam na dane od klienta: ");
                int answerSize = inFromClient.read(answer);
                if (answerSize == -1) break;
                System.out.println("#" + id + " Message: " + new String(answer, 0, answerSize));
                System.out.println("Size: " + answerSize);
                outToClient.write(answer, 0, answerSize);
            } catch (Exception e) {
                System.out.println("id: " + id + " Connection interrupted");
                break;
            }
        }
        clients.remove(this);
        System.out.println("id: " + id + " Koniec klienta: ");
    }
}
