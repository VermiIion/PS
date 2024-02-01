package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter server IP address: ");
            String serverIP = scanner.nextLine();
            System.out.print("Enter server port: ");
            int port = scanner.nextInt();

            InetAddress serverAddress = InetAddress.getByName(serverIP);

            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            while (true) {
                System.out.print("Enter message to send (type 'exit' to stop): ");
                String message = scanner.next();

                if ("exit".equalsIgnoreCase(message)) {
                    break;
                }

                byte[] buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);
                socket.send(packet);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}