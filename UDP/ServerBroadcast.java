package UDP;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerBroadcast {
    protected int maxClients;
    protected int serverPort;

    public ServerBroadcast(int serverPort, int maxClients) {
        this.serverPort = serverPort;
        this.maxClients = maxClients;
    }


    public static void main(String[] argv) throws Exception {
        int maxClients = 3;
        int port = 7;
        port = ClientBroadcast.inputPort(port, validatePort(port));
        ServerBroadcast serverBroadcast = new ServerBroadcast(port,maxClients);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("Server started");
        do {

        } while (true);
    }
    public static boolean validatePort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
