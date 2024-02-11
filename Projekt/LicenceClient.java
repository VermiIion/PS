import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    public LicenceClient() {
        this.serverIP = "";
        this.serverPort = 0;
        this.licenceTokenValid = false;
        this.licenceToken = "";
        this.currentLicenceUserName = "";
        this.currentLicenceKey = "";
        this.socket = null;
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
                out.println("Licence Username: " + currentLicenceUserName + " Licence Key: " + currentLicenceKey);

                // Odczytanie odpowiedzi od serwera
                String response = in.readLine();
                if (response.startsWith("LICENCE_TOKEN")) {
                    licenceToken = response.split(" ")[1];
                    licenceTokenValid = true;

                    // Ustawienie timera do odnowienia tokena po upływie czasu ważności
                    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.schedule(() -> {
                        licenceTokenValid = false;
                        System.out.println("Licence token expired.");
                    }, 600, TimeUnit.SECONDS); // 600 sekund - czas ważności tokena licencji
                } else {
                    System.out.println("Error while getting licence token: " + response);
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

        // Przykładowe użycie API Klienta Licencji
        licenceClientAPI.start("localhost", 8080); // Startuje klienta z adresem serwera i portem
        licenceClientAPI.setLicence("Radek", "9F3A0874-5C23449A-53FC05D6-8EDA1E1B"); // Ustawia nową licencję
        String licenceToken = licenceClientAPI.getLicenceToken(); // Pobiera token licencji
        System.out.println("Licence token: " + licenceToken);
        licenceClientAPI.stop(); // Zatrzymuje klienta
    }
}