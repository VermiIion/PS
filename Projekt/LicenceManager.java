package Projekt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class LicenceManager {
    private final Map<String, Licence> licences;

    public LicenceManager(String filename) throws IOException {
        this.licences = loadLicencesFromFile(filename);
    }

    private Map<String, Licence> loadLicencesFromFile(String filename) throws IOException {
        Map<String, Licence> licences = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder jsonContent = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }
        // Parse JSON and populate licences map
        // Implementation omitted for brevity
        return licences;
    }

    public Licence getLicenceByKey(String licenceKey) {
        // Implementation omitted for brevity
        return null;
    }

    public String generateLicenceKey(String userName) throws NoSuchAlgorithmException {
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

    public boolean validateLicence(String userName, String licenceKey, String clientIP) {
        // Implementation omitted for brevity
        return false;
    }

    // Other methods for managing licences
}

