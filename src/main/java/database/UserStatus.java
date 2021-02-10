package database;

import java.util.HashSet;
import java.util.Set;

public class UserStatus {

    private static final Set<Token> loggedInUsers = new HashSet<>();

    public static Set<Token> getLoggedInUsers() {
        return loggedInUsers;
    }

}
