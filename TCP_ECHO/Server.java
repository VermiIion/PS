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
        Server server = new Server(port,maxClients);
        ArrayList<ClientContainer> clients = new ArrayList<ClientContainer>();
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(7));
        int closeFlag = 0;
        while (true){
            if(!clients.isEmpty()) {
                for (ClientContainer c : clients) {
                    if (!c.running) {
                        c.clientSocket.close();
                        clients.remove(c);
                    }
                }
                if(clients.isEmpty()) closeFlag = 1;
            }
            Socket cleanSocket = new Socket();
            cleanSocket = serverSocket.accept();
            if(clients.size() == server.maxClients){
                new ClientContainer(cleanSocket, clients.size() + 1, false);
                continue;
            }
            ClientContainer client = new ClientContainer(cleanSocket, clients.size() + 1, true);
            client.start();
            clients.add(client);
            if(closeFlag == 1) break;
        }
        serverSocket.close();
    }
}
