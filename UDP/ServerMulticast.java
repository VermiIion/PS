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

    }
    public static boolean validatePort(final int port) {
        return port >= 0 && port <= 65535;
    }
}
