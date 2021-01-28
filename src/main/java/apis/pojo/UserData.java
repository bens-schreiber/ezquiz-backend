package apis.pojo;

public class UserData {

    private String password;
    private String username;
    private int quizKey;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private int score;

    public int getQuizKey() {
        return quizKey;
    }

    public void setQuizKey(int quizKey) {
        this.quizKey = quizKey;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
