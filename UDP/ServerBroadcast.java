package UDP;

import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ServerBroadcast {
    public static void main(String[] args) throws Exception{
        DatagramSocket server = new DatagramSocket(4160);
        byte[] buf = new byte[1000];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        while(true) {
            server.receive(p);
            System.out.println(Arrays.toString(p.getData()));
            if(Arrays.equals(p.getData(), "x".getBytes())) break;
            server.send(p);
        }
        server.close();
    }
//    protected int maxClients;
//    protected int serverPort;
//
//    public ServerBroadcast(int serverPort, int maxClients) {
//        this.serverPort = serverPort;
//        this.maxClients = maxClients;
//    }
//
//
//    public static void main(String[] argv) throws Exception {
//        int maxClients = 3;
//        int port = 7;
//        port = ClientBroadcast.inputPort(port, validatePort(port));
//        ServerBroadcast serverBroadcast = new ServerBroadcast(port,maxClients);
//        ServerSocket serverSocket = new ServerSocket();
//        serverSocket.bind(new InetSocketAddress(port));
//        System.out.println("Server started");
//        do {
//
//        } while (true);
//    }
//    public static boolean validatePort(final int port) {
//        return port >= 0 && port <= 65535;
//    }
}
