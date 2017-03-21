package database;

import yamb.YambApp;

import java.sql.*;

/**
 * Created by Alex Pryakhin on 14.03.2017.
 */
public class DatabaseHandler {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    static{
        try {
            Class.forName("org.sqlite.JDBC");
            String db_location = YambApp.getConfig().getStringParameter("sqlite", "db_location");
            String db_name = YambApp.getConfig().getStringParameter("sqlite", "db_name");

            System.out.println(db_location + db_name);

            connection = DriverManager.getConnection("jdbc:sqlite:" + db_location + db_name);
        }
        catch (ClassNotFoundException e){
            System.err.println("Class for JDBC was not found");
        }
        catch (SQLException e){
            System.err.println("SQLException : " + e.getMessage());
        }

        System.out.println("Connection created");
    }

    /**
     * Returns query result as ResultSet object
     * @param query - well-formed SQL query
     * @return ResultSet collection
     * @throws SQLException - if something goes wrong
     */
    public static ResultSet getQueryResult(String query) throws SQLException {
        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        statement.close();

        return resultSet;
    }

    /**
     * Executes SQL-query like INSERT, UPDATE or DELETE without result return
     * @param query - well-formed SQL-query
     * @throws SQLException - if something goes wrong
     */
    public static void executeQuery(String query) throws SQLException {
        statement = connection.createStatement();
        statement.executeQuery(query);
        statement.close();
    }
}
