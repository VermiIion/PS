import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LicenceServer {
    private final int tcpPort;
    private final Map<String, Licence> licences = new HashMap<>();
    private Map<String,Integer> countLicenses  = new Hashtable<String,Integer>();;
    ServerSocket serverSocket;
    ScheduledExecutorService executorService;
    List<Socket> clientSockets;

    public LicenceServer(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void start() {
        loadLicencesFromFile("./Projekt/licenses.json");

        Thread tcpThread = new Thread(this::startTcpServer);
        tcpThread.start();

        System.out.println("TCP Server started on port " + tcpPort);
    }

    public void stop() {
        try {
            // Zamknij gniazdo serwera
            serverSocket.close();

            // Zamknij wszystkie otwarte gniazda klientów
            for (Socket clientSocket : clientSockets) {
                clientSocket.close();
            }

            // Zakończ executorService, aby uniknąć utraty zasobów
            executorService.shutdown();

            // Oczekuj na zakończenie wszystkich zadań zaplanowanych w executorService
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                // Jeśli po 10 sekundach zadania nadal działają, zakończ je natychmiast
                executorService.shutdownNow();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadLicencesFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            JSONObject jsonObject = new JSONObject(jsonContent.toString());
            JSONArray licencesArray = jsonObject.getJSONArray("payload");
            for (int i = 0; i < licencesArray.length(); i++) {
                JSONObject licenceObject = licencesArray.getJSONObject(i);
                String userName = licenceObject.getString("LicenceUserName");
                long validationTime = licenceObject.getLong("ValidationTime");
                int count = licenceObject.getInt("Licence");
                licences.put(userName, new Licence(userName, validationTime, count));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startTcpServer() {
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleTcpClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleTcpClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            StringBuilder requestBuilder = new StringBuilder();
            String line;
            line = in.readLine();
            requestBuilder.append(line);
            String request = requestBuilder.toString();

            // Parsowanie żądania
            JSONObject jsonRequest = new JSONObject(request);
            String userName = jsonRequest.getString("Licence Username");
            String licenceKey = jsonRequest.getString("Licence Key");
            licenceKey = licenceKey.replaceAll("-","");
            // Weryfikacja klucza licencji
            if (verifyLicenceKey(userName, licenceKey)) {
                // Jeśli klucz jest poprawny
                Licence licence = licences.get(userName);
                if (licence != null) {
                    // Jeśli licencja jest ważna
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("LicenceUserName", userName);
                    responseJson.put("Licence", true);
                    if(userName.equals("Admin")) {
                        responseJson.put("Expired",  "Infinite");
                        out.println(responseJson.toString());
                        System.out.println("Licence token for: " + userName + "\nExpires at: " + "Infinite");
                    }
                    else {
                        if(!this.countLicenses.isEmpty()) {
                            if (this.licences.get(userName).count <= countLicenses.get(userName)) {
                                responseJson.put("LicenceUserName", userName);
                                responseJson.put("Licence", false);
                                responseJson.put("Description", "Przekroczono ilość licencji dla użytkownika " + userName);
                                out.println(responseJson.toString());
                            }
                            else{
                                this.countLicenses.put(userName, this.countLicenses.get(userName) + 1);
                                LocalDateTime expiration = LocalDateTime.ofEpochSecond((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + licence.validationTime), 0, ZoneOffset.UTC);
                                responseJson.put("Expired", expiration);
                                out.println(responseJson.toString());
                                System.out.println("Licence token for: " + userName + "\nExpires at: " + expiration);
                                ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                                executorService.schedule(() -> {
                                    if (this.countLicenses.get(userName) > 0)
                                        this.countLicenses.put(userName, this.countLicenses.get(userName) - 1);
                                    else this.countLicenses.remove(userName);
                                    System.out.println("Licence token expired.");
                                }, expiration.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), TimeUnit.SECONDS);
                            }
                        }
                        else {
                            this.countLicenses.put(userName, 1);
                            LocalDateTime expiration = LocalDateTime.ofEpochSecond((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + licence.validationTime), 0, ZoneOffset.UTC);
                            responseJson.put("Expired", expiration);
                            out.println(responseJson.toString());
                            System.out.println("Licence token for: " + userName + "\nExpires at: " + expiration);
                            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                            executorService.schedule(() -> {
                                if (this.countLicenses.get(userName) > 0)
                                    this.countLicenses.put(userName, this.countLicenses.get(userName) - 1);
                                else this.countLicenses.remove(userName);
                                System.out.println("Licence token expired.");
                            }, expiration.toEpochSecond(ZoneOffset.UTC) - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), TimeUnit.SECONDS);
                        }

                    }
                } else {
                    // Jeśli nie ma takiej licencji lub jest nieważna
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("LicenceUserName", userName);
                    responseJson.put("Licence", false);
                    responseJson.put("Description", "Brak ważnej licencji dla użytkownika " + userName);
                    out.println(responseJson.toString());
                }
            } else {
                // Jeśli klucz jest niepoprawny
                JSONObject responseJson = new JSONObject();
                responseJson.put("LicenceUserName", userName);
                responseJson.put("Licence", false);
                responseJson.put("Description", "Niepoprawny klucz licencji dla użytkownika " + userName);
                out.println(responseJson.toString());
            }

            // Zamykanie połączenia z klientem
            //clientSocket.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private boolean verifyLicenceKey(String userName, String licenceKey) throws NoSuchAlgorithmException {
        // Tutaj możemy zaimplementować logikę weryfikacji klucza licencji
        // Wygeneruj klucz licencji na podstawie nazwy użytkownika
        String generatedKey = generateLicenceKey(userName);

        // Porównaj wygenerowany klucz z przesłanym kluczem
        return generatedKey.equals(licenceKey);
    }

    private boolean isValidLicence(Licence licence) {
        // Sprawdź czy licencja jest ważna, porównując czas ważności z aktualnym czasem
        return licence.getValidationTime() > System.currentTimeMillis() / 1000;
    }

    private String getExpirationTime(Licence licence) {
        // Zwróć czas wygaśnięcia licencji w formacie ISO 8601
        return LocalDateTime.now().plusSeconds(licence.getValidationTime())
                .format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private String generateLicenceKey(String userName) throws NoSuchAlgorithmException {
        String input = userName;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString().toUpperCase();
    }


    private static class Licence {
        private final String userName;
        private final long validationTime;
        private final int count;

        public Licence(String userName, long validationTime, int count) {
            this.userName = userName;
            this.validationTime = validationTime;
            this.count = count;
        }

        public String getUserName() {
            return userName;
        }

        public long getValidationTime() {
            return validationTime;
        }
    }

    public static void main(String[] args) {
        LicenceServer licenceServer = new LicenceServer(8080);
        licenceServer.start();
    }
}
