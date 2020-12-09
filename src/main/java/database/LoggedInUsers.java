package database;

import java.util.HashMap;
import java.util.UUID;

public class LoggedInUsers {

    private static HashMap<String, String> loggedInUsers = new HashMap<>();

    public static HashMap<String, String> getLoggedInUsers() {
        return loggedInUsers;
    }
}
