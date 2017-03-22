package database;

import telegram.TelegramSubscriber;
import yamb.YambApp;

import java.sql.*;

/**
 * Created by Alex Pryakhin on 14.03.2017.
 */
public class DatabaseHandler {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void initDatabase(){
        try {
            Class.forName("org.sqlite.JDBC");
            String db_location = YambApp.getConfig().getStringParameter("sqlite", "db_location");
            String db_name = YambApp.getConfig().getStringParameter("sqlite", "db_name");

            connection = DriverManager.getConnection("jdbc:sqlite:" + db_location + db_name);
        }
        catch (ClassNotFoundException e){
            System.err.println("Class for JDBC was not found");
        }
        catch (SQLException e){
            System.err.println("SQLException : " + e.getMessage());
        }
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
    }

    /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>,
     * <code>UPDATE</code>, or <code>DELETE</code> statement or an
     * SQL statement that returns nothing, such as an SQL DDL statement.
     *<p>
     * <strong>Note:</strong>This method cannot be called on a
     * <code>PreparedStatement</code> or <code>CallableStatement</code>.
     * @param query an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
     *
     * @exception SQLException if a database access error occurs,
     * this method is called on a closed <code>Statement</code>, the given
     * SQL statement produces a <code>ResultSet</code> object, the method is called on a
     * <code>PreparedStatement</code> or <code>CallableStatement</code>
     * @throws SQLTimeoutException when the driver has determined that the
     * timeout value that was specified by the {@code setQueryTimeout}
     * method has been exceeded and has at least attempted to cancel
     * the currently running {@code Statement}
     */
    public static void executeUpdate(String query) throws SQLException, SQLTimeoutException{
        statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }
}
