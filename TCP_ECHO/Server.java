package TCP_ECHO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    protected int maxClients = 3;
    protected int serverPort = 7;
    protected int connectedClients = 0;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    public Server(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
    }


    public static void main(String[] argv) throws Exception {
        int maxClients = 3;
        int port = 7;
        Server server = new Server(7,maxClients);
        ArrayList<ClientContainer> clients = new ArrayList<ClientContainer>();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(7));
        while (true){
            Socket cleanSocket = new Socket();
            cleanSocket = serverSocket.accept();
            if(clients.size() == server.maxClients){
                new ClientContainer(cleanSocket, clients.size() + 1, false);
                continue;
            }
            ClientContainer client = new ClientContainer(cleanSocket, clients.size() + 1, true);
            client.run();
            clients.add(client);
            for(ClientContainer c : clients){
                if(c.running == false)
            }
        }

    }
}
