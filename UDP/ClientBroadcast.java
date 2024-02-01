package UDP;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class ClientBroadcast {
    public static void main(String[] argv) throws IOException {
        DatagramSocket client = new DatagramSocket();
        InetAddress adress = InetAddress.getByName("localhost");
        Scanner input = new Scanner(System.in);
        String message = "";
        while(true) {
            message = input.nextLine();
            byte[] buf = message.getBytes();
            DatagramPacket p = new DatagramPacket(buf, buf.length, adress, 4160);
            client.send(p);
            System.out.println(Arrays.toString(p.getData()));
            client.receive(p);
            if(Arrays.equals(p.getData(), "x".getBytes())) break;
        }
        client.close();;
    }
}

