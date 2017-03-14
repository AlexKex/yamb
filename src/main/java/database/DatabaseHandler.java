package database;

import java.sql.*;

/**
 * Created by Alex Pryakhin on 14.03.2017.
 */
public class DatabaseHandler {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public DatabaseHandler(String db_name) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:"+db_name);

        System.out.println("DB has been connected");
    }
}
