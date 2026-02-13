package utils;


import java.sql.*;
import java.util.*;

public class DBUtilsUpdated {

    private static String dbUrl = "jdbc:mysql://172.25.115.100:3311/property"; // <-- update schema name
    private static String dbUser = "apptesting_user";
    private static String dbPassword = "App123Test@r321";

    // Get DB Connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Run any SELECT query with parameters and return result as List of Maps
     * @param query SQL query with ? placeholders
     * @param params Parameters to replace ?
     * @return List of rows (each row is Map<column, value>)
     */
    public static List<Map<String, Object>> runSelectQuery(String query, Object... params) {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            System.out.println("Executing query: " + query);
            try (ResultSet rs = pstmt.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnCount = meta.getColumnCount();

                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(meta.getColumnLabel(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
   
}

