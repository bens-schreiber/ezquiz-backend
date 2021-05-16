package classes.database;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import classes.etc.Constants;
import org.json.JSONObject;

import java.sql.*;

public class QueryExecutor {

    private static BoneCP connectionPool;
    static {
        setUpconnectionPool();
    }

//    private static Connection getConnection() {
//        Connection conn = null;
//        try {
//            conn = connectionPool.getConnection();
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//
//        return conn;
//    }

    public static int executeUpdateQuery(String query, Object... args) {
        try {

            Connection con = connectionPool.getConnection();

            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            //Assemble a prepared statement if there are args
            PreparedStatement stmt = con.prepareStatement(query);
            int i = 1;
            for (Object obj : args) {
                if (obj instanceof Integer) {
                    stmt.setInt(i++, (Integer) obj);
                } else if (obj instanceof String) {
                    stmt.setString(i++, (String) obj);
                }
            }

            int response = stmt.executeUpdate();

            stmt.close();
            con.close();

            return response;


        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }

        return -1;
    }

    public static JSONObject runQuery(String query) {
        JSONObject jsonObject = new JSONObject();
        try {

            Connection con = connectionPool.getConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the database");
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Use this for getting col names
            ResultSetMetaData rsmd = rs.getMetaData();
            int objCount = 0;
            int index;
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject tmp = new JSONObject();
                for (index = 0; index < numColumns; ++index) {
                    String columnName = rsmd.getColumnName(index + 1);
                    Object obj = rs.getObject(index + 1);

                    if (obj == null) {
                        continue;
                    }

                    tmp.put(columnName, obj.toString());
                }
                // add this object to the main json object
                jsonObject.put("obj" + objCount++, tmp);
            }

            rs.close();
            con.close();
            stmt.close();

        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        }

        return jsonObject;
    }


    private static synchronized void setUpconnectionPool() {
        System.out.println("setting up connection");
        Connection connection;
        try {
            // Set up the connection pool
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(Constants.getDbPath());
            config.setUsername(Constants.adminUser);
            config.setPassword(Constants.adminPassword);
            config.setMinConnectionsPerPartition(Constants.CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION);
            config.setMaxConnectionsPerPartition(Constants.CONNECTION_POOL_MAX_CONNECTIONS_PER_PARTITION);
            config.setPartitionCount(Constants.CONNECTION_POOL_PARTITION_COUNT);

            connectionPool = new BoneCP(config);

            connection = connectionPool.getConnection(); // fetch a connection

            if (connection != null) {
                // Assign connection pool to current server.thread
                connection.close();
                System.out.println("Successfully connected to database: " + Constants.getDbPath());
            } else {
                System.err.println("Failed to establish a connection with database: " + Constants.getDbPath());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}