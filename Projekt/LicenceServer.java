import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LicenceServer {
    private final int tcpPort;
    private final Map<String, Licence> licences = new HashMap<>();

    public LicenceServer(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public void start() {
        loadLicencesFromFile("./Projekt/licenses.json");

        Thread tcpThread = new Thread(this::startTcpServer);
        tcpThread.start();

        System.out.println("TCP Server started on port " + tcpPort);
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
                licences.put(userName, new Licence(userName, validationTime));
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
            String userName = jsonRequest.getString("LicenceUserName");
            String licenceKey = jsonRequest.getString("LicenceKey");

            // Weryfikacja klucza licencji
            if (verifyLicenceKey(userName, licenceKey)) {
                // Jeśli klucz jest poprawny
                Licence licence = licences.get(userName);
                if (licence != null && isValidLicence(licence)) {
                    // Jeśli licencja jest ważna
                    JSONObject responseJson = new JSONObject();
                    responseJson.put("LicenceUserName", userName);
                    responseJson.put("Licence", true);
                    responseJson.put("Expired", getExpirationTime(licence));
                    out.println(responseJson.toString());
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
            clientSocket.close();
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
        String input = userName + LocalDateTime.now().toString();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    private static class Licence {
        private final String userName;
        private final long validationTime;

        public Licence(String userName, long validationTime) {
            this.userName = userName;
            this.validationTime = validationTime;
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
