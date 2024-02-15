import org.json.JSONObject;

import javax.swing.plaf.synth.SynthTabbedPaneUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LicenceClient {
    private String serverIP;
    private int serverPort;
    private boolean licenceTokenValid;
    private String licenceToken;
    private String currentLicenceUserName;
    private String currentLicenceKey;
    private Socket socket;
    private ScheduledExecutorService executorService;

    public LicenceClient() {
        this.serverIP = "";
        this.serverPort = 0;
        this.licenceTokenValid = false;
        this.licenceToken = "";
        this.currentLicenceUserName = "";
        this.currentLicenceKey = "";
        this.socket = null;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public void start(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        try {
            // Tworzenie połączenia TCP na lokalnym hoście
             this.socket = new Socket(serverIP, serverPort);
            // Tutaj można dodać kod obsługi połączenia
            System.out.println("Started Licence Client API. Server IP: " + serverIP + ", Port: " + serverPort);
        } catch (IOException e) {
            System.out.println("Error while connecting to the server: " + e.getMessage());
        }
    }

    public void setLicence(String userName, String licenceKey) {
        this.currentLicenceUserName = userName;
        this.currentLicenceKey = licenceKey;
        // Tutaj umieść logikę ustawiania nowej licencji
        System.out.println("Licence set for user: " + userName);
    }

    public String getLicenceToken() {
        if (!licenceTokenValid) {
            try {
                // Wysłanie żądania pobrania tokenu licencji na serwer
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                JSONObject message = new JSONObject();
                message.put("Licence Username", currentLicenceUserName);
                message.put("Licence Key", currentLicenceKey);
                out.println(message.toString());

                // Odczytanie odpowiedzi od serwera
                JSONObject response = new JSONObject(in.readLine());
                if (response.getBoolean("Licence")) {
                    licenceTokenValid = true;
                    String expiration = response.getString("Expired");
                    if (!expiration.equals("Infinite")) {
                        LocalDateTime expirationTime = LocalDateTime.parse(response.getString("Expired"));
                        licenceToken = expirationTime.toString();
                        // Ustawienie timera do odnowienia tokena po upływie czasu ważności
                        executorService.schedule(() -> {
                            licenceTokenValid = false;
                            System.out.println("Licence token expired.");
                            System.out.println("Getting new token.");
                            getLicenceToken();
                        }, expirationTime.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), TimeUnit.SECONDS); // 600 sekund - czas ważności tokena licencji
                    }else licenceToken = "Infinite";
                }
                else {
                    System.out.println(response.getString("Description"));
                    stop();
                    System.exit(0);
                }
            } catch (IOException e) {
                System.out.println("Error while communicating with the server: " + e.getMessage());
            }
        }
        return licenceToken;
    }

    public void stop() {
        // Tutaj umieść logikę zakończenia działania klienta licencji
        System.out.println("Stopping Licence Client API.");
        this.serverIP = "";
        this.serverPort = 0;
        this.licenceTokenValid = false;
        this.licenceToken = "";
        this.currentLicenceUserName = "";
        this.currentLicenceKey = "";
        this.executorService.shutdownNow();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing the socket: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LicenceClient licenceClientAPI = new LicenceClient();

        licenceClientAPI.start("localhost", 8080); // Startuje klienta z adresem serwera i portem
        licenceClientAPI.setLicence("Radek", "9F3A08745C23449A53FC05D68EDA1E1B"); // Ustawia nową licencję
        String licenceToken = licenceClientAPI.getLicenceToken(); // Pobiera token licencji
        System.out.println("Licence token: " + licenceToken);
        Scanner scanner = new Scanner(System.in);
        System.out.println("end");
        scanner.next();
        licenceClientAPI.stop(); // Zatrzymuje klienta
        System.out.println("ended");
    }
}