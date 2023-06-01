package UDP;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerMulticast {

    protected int serverPort;

    public ServerMulticast(int serverPort, int maxClients) {
        this.serverPort = serverPort;

    }


    public static void main(String[] argv) throws Exception {
        int maxClients = 3;
        int port = 7;
        port = ClientBroadcast.inputPort(port, validatePort(port));
        ServerMulticast server = new ServerMulticast(port,maxClients);
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("Server started");
        do {
            Socket cleanSocket = serverSocket.accept();


        } while (true);
    }
    public static boolean validatePort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
