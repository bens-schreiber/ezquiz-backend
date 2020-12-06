package database;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import etc.Constants;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class QueryExecutor {

    private static BoneCP connectionPool;

    static {
        setUpconnectionPool();
    }

    private static Connection getConnection() {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            //System.out.println("Obtained connection from connection pool.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }


    public static JSONObject runQuery(String query) throws SQLException {
        Statement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        ArrayList<String> ret = new ArrayList<>();
        try {
            con = getConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            // Use this for gettting col names
            ResultSetMetaData rsmd = rs.getMetaData();
            int objCount = 0;
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
                jsonObject.put("obj" + Integer.toString(objCount++), tmp);
            }
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {

            }
            try {
                rs.close();
            } catch (SQLException e) {

            }
            try {
                con.close();
            } catch (SQLException e) {

            }
        }
        return jsonObject;
    }

    public static boolean execute(String query) throws SQLException {
        Statement stmt = null;
        Connection con = null;
        boolean executed = false;

        try {
            con = getConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            stmt.execute(query);
            executed = true;
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            /* Close an clean up connection. Upon closing this connection, we will
               return the connection back to the connection pool.
             */
            try {
                stmt.close();
            } catch (SQLException e) {

            }
        }
        try {
            con.close();
        } catch (SQLException e) {

        }

        return executed;
    }


    private static synchronized void setUpconnectionPool() {
        Connection connection = null;
        try {
            // Set up the connection pool
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(Constants.getDbPath(true));
            config.setUsername(Constants.adminUser);
            config.setPassword(Constants.adminPassword);
            config.setMinConnectionsPerPartition(Constants.CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION);
            config.setMaxConnectionsPerPartition(Constants.CONNECTION_POOL_MAX_CONNECTIONS_PER_PARTITION);
            config.setPartitionCount(Constants.CONNECTION_POOL_PARTITION_COUNT);
            connectionPool = new BoneCP(config);

            connection = connectionPool.getConnection(); // fetch a connection

            if (connection != null) {
                // Assign connection pool to current server.thread
                System.out.println("Successfully connected to database: " + Constants.getDbPath(true));
                QueryExecutor.connectionPool = connectionPool;
            } else {
                System.err.println("Failed to establish a connection with database: " + Constants.getDbPath(true));
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}