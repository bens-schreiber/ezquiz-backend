package database;

import java.util.List;

public class ActiveTests {

    private static List<String> activeTokens;

    public void addToken(String token) {
        activeTokens.add(token);
    }

    public static List<String> getActiveTokens() {
        return activeTokens;
    }
}
