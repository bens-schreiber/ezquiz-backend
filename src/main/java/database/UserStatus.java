package database;

import java.util.HashSet;
import java.util.Set;

public class UserStatus {

    private static final Set<String> loggedInUsers = new HashSet<>();

    public static Set<String> getLoggedInUsers() {
        return loggedInUsers;
    }

//    private static final Set<String> usersInQuizzes = new HashSet<>();
//
//    public static Set<String> getUsersInQuizzes() {
//        return usersInQuizzes;
//    }
}
