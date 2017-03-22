package telegram;

import database.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Subscriber model
 * Created by Alex Pryakhin on 22.03.2017.
 */
public class TelegramSubscriber {
    private Long userId;
    private String userName;
    private Long chatId;
    private boolean isActive = false;

    public TelegramSubscriber(Long userId, String userName, Long chatId){
        this.userId = userId;
        this.userName = userName;
        this.chatId = chatId;
        this.isActive = true;
    }

    public static HashMap<Long, TelegramSubscriber> restoreSubscribersList(){
        HashMap<Long, TelegramSubscriber> result= new HashMap<>();

        try {
            String query = "SELECT * FROM subscribers";
            ResultSet resSet = DatabaseHandler.getQueryResult(query);

            while(resSet.next())
            {
                result.put(
                        resSet.getLong("id"),
                        new TelegramSubscriber(
                                resSet.getLong("id"),
                                resSet.getString("user_name"),
                                resSet.getLong("id_chat")
                        )
                );
            }
        }
        catch (SQLException e){
            System.err.println("Unable to restore subscribers");
            System.err.println(e.getMessage());
        }

        return result;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate(){
        this.isActive = false;

        try {
            String query = "UPDATE subscribers SET is_active = 'false' WHERE id = ";
            query += "'" + userId + "'";
            DatabaseHandler.executeUpdate(query);
        }
        catch (SQLException e){
            System.err.println("Can's deactivate subscriber");
            System.err.println(e.getMessage());
        }
    }

    public void activate(){
        this.isActive = true;

        try {
            String query = "UPDATE subscribers SET is_active = 'true' WHERE id = ";
            query += "'" + userId + "'";
            DatabaseHandler.executeUpdate(query);
        }
        catch (SQLException e){
            System.err.println("Can's activate subscriber");
            System.err.println(e.getMessage());
        }
    }

    public void saveSubscriberToDB(){
        try {
            String query = "INSERT INTO subscribers VALUES (";
            query += userId + ",";
            query += "'" + userName + "',";
            query += chatId + ",";
            query += "'" + isActive + "')";
            DatabaseHandler.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Can't save subscriber to the DB");
            System.err.println(e.getMessage());
        }
    }

    public void removeSubscriberFromDB(){
        try {
            String query = "DELETE FROM subscribers WHERE id = ";
            query += "'" + userId + "'";
            DatabaseHandler.executeUpdate(query);
        }
        catch (SQLException e){
            System.err.println("Can's remove subscriber from the DB");
            System.err.println(e.getMessage());
        }
    }
}
