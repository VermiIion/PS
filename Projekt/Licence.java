package Projekt;

import java.util.List;

class Licence {
    private final String userName;
    private final int maxSeats;
    private final List<String> allowedIPs;
    private final long validationTime;

    public Licence(String userName, int maxSeats, List<String> allowedIPs, long validationTime) {
        this.userName = userName;
        this.maxSeats = maxSeats;
        this.allowedIPs = allowedIPs;
        this.validationTime = validationTime;
    }

    public String getUserName() {
        return userName;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public List<String> getAllowedIPs() {
        return allowedIPs;
    }

    public long getValidationTime() {
        return validationTime;
    }
}

