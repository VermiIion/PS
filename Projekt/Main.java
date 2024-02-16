package Projekt;


import org.json.JSONObject;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LicenceClient client = new LicenceClient();
        String ip = "";
        int port;
        System.out.println("Wpisz IP");
        ip = scanner.nextLine();
        System.out.println("Wpisz port");
        port = scanner.nextInt();
        client.start("localhost",8080);
        // Admin E3AFED0047B08059D0FADA10F400C1E5
        // Radek 9F3A08745C23449A53FC05D68EDA1E1B
        client.setLicence("Radek", "9F3A08745C23449A53FC05D68EDA1E1B");
        System.out.println(client.getLicenceToken());
        System.out.println("end");
        scanner.next();
        client.stop();
        System.out.println("ended");
    }
}
