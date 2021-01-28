package database;

import java.util.HashMap;

public class LoggedInUsers {

    private static final HashMap<String, String> loggedInUsers = new HashMap<>();

    public static HashMap<String, String> getLoggedInUsers() {
        return loggedInUsers;
    }
}
