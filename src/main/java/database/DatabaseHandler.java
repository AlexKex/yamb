package database;

import java.sql.*;

/**
 * Created by Alex Pryakhin on 14.03.2017.
 */
public class DatabaseHandler {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public DatabaseHandler(String db_name) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:"+db_name);

        System.out.println("DB has been connected");
    }

    public static ResultSet getQueryResult(String query) throws SQLException {
        resultSet = statement.executeQuery(query);

        return resultSet;
    }

    public static void executeQuery(String query) throws SQLException {
        statement.executeQuery(query);
    }
}
