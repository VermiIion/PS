package TCP_ECHO;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    protected int maxClients;
    protected int serverPort;

    public Server(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
    }


    public static void main(String[] argv) throws Exception {
        int maxClients = 3;
        int port = 7;
        port = Client.inputPort(port, validatePort(port));
        Server server = new Server(port,maxClients);
        ArrayList<ClientContainer> clients = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("Server started");
        do {
            Socket cleanSocket = serverSocket.accept();
            System.out.println("Klientów: " + clients.size());
            System.out.println("-----");
            ClientContainer client;
            if (clients.size() >= server.maxClients)
                client = new ClientContainer(cleanSocket, clients, false);
            else client = new ClientContainer(cleanSocket, clients, true);
            client.start();

        } while (true);
    }
    public static boolean validatePort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
