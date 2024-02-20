package Projekt;


import org.json.JSONObject;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LicenceClient client = new LicenceClient();
        String ip = "";
        int port;
        try {
            System.out.println("Wpisz IP");
            ip = scanner.nextLine();
            System.out.println("Wpisz port");
            port = scanner.nextInt();
        }catch (InputMismatchException e){
            System.out.println("Niewłaściwy Port");
            System.exit(1);
        }
        client.start("localhost",8080);
        // Admin E3AFED0047B08059D0FADA10F400C1E5
        // Radek 9F3A08745C23449A53FC05D68EDA1E1B
        client.setLicence("Radek", "9F3A08745C23449A53FC05D68EDA1E1B");
        System.out.println(client.getLicenceToken());
        System.out.println("wpisz cokolwiek aby zakończyć program");
        scanner.next();
        client.stop();
        System.out.println("ended");
    }
}
