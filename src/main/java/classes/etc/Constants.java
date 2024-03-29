package classes.etc;

public class Constants {
    static public int JETTY_PORT_NUMBER = 7080;
    static public String API_PATH = "/api";
    static public String JDBC = "jdbc";
    static public String LOCAL_HOST = "localhost";

    static public String DB_HOST = "";
    static public int CONNECTION_POOL_MAX_CONNECTIONS_PER_PARTITION = 10;
    static public int CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION = 5;
    static public int CONNECTION_POOL_PARTITION_COUNT = 1;
    static public String PASSWORD_HASH_BLOWFISH = "2a";

    static public String adminUser = "root";
    static public String adminPassword = "root";

    static public String NO_ANSWER_QUERY = "select id, question, options, directions, type from question";

    static public String getDbPath() {
//         if (localHost) {
//            return Constants.JDBC + ":" + "mysql" + "://" + LOCAL_HOST + ':' + "3306" + "/" + "questions_db";
//        }

        String host = LOCAL_HOST;
        if (System.getenv().containsKey("mysqlhost")) { host = System.getenv("mysqlhost"); }
        System.out.println(host);

        return Constants.JDBC + ":" + "mysql" + "://" + host + ':' + "3306" + "/" + "questions_db";
    }

}