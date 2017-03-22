package telegram;

import database.DatabaseHandler;
import org.telegram.telegrambots.api.objects.Update;
import yamb.YambApp;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

/**
 * Created by Alex Pryakhin on 22.03.2017.
 */
public class TelegramUser {
    private Long userId;
    private String userName;
    private Long chatId;
    private boolean isAdmin = false;
    private static HashMap<Long, TelegramUser> adminList = new HashMap<>();

    public static void initAdmins(){
        try {
            String sql = "SELECT * FROM users WHERE is_admin = 'true'";
            ResultSet adminRS = DatabaseHandler.getQueryResult(sql);

            while (adminRS.next()){
                adminList.put(adminRS.getLong("id"), new TelegramUser(
                        adminRS.getString("user_name"),
                        adminRS.getLong("id"),
                        adminRS.getLong("id_chat"),
                        true
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error in admin list creation. Admin actions will be unavailable");
            System.err.println(e.getMessage());
        }
    }

    public TelegramUser(String userName, Long userId, Long chatId){
        this.userId = userId;
        this.userName = userName;
        this.chatId = chatId;

        if(!checkForAdminRightsInDatabase()) {
            if (checkForAdminRightsInConfig(this.userName)) {
                isAdmin = true;
            }
        }
        else{
            isAdmin = true;
        }
    }

    public TelegramUser(String userName, Long userId, Long chatId, boolean isAdmin){
        this.userId = userId;
        this.userName = userName;
        this.chatId = chatId;
        this.isAdmin = isAdmin;
    }

    public TelegramUser(Update update){
        this.userId = update.getMessage().getFrom().getId().longValue();
        this.userName = update.getMessage().getFrom().getUserName();
        this.chatId = update.getMessage().getChatId();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Long getChatId() { return chatId; }

    public static HashMap<Long, TelegramUser> getAdminsList(){
        return adminList;
    }

    public void register(){
        try {
            //save admin in DB
            String query = "INSERT INTO users VALUES (";
            query += userId + ",";
            query += "'" + userName + "',";
            query += "'" + isAdmin + "',";
            query += chatId + ")";
            DatabaseHandler.executeUpdate(query);

            TelegramSubscriber adminTS = new TelegramSubscriber(userId, userName, chatId);
            adminTS.saveSubscriberToDB();

            adminList.put(userId, this);
        } catch (SQLException e) {
            System.err.println("Can't save user to the DB");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Check user table for admin rights
     * @return isAdmin flag
     */
    private boolean checkForAdminRightsInDatabase(){
        boolean userIsAdmin = false;

        String sql = "SELECT is_admin FROM users WHERE id = " + userId;
        try {
            ResultSet adminResultSet = DatabaseHandler.getQueryResult(sql);

            while(adminResultSet.next())
            {
                if(adminResultSet.getBoolean("is_admin")){
                    userIsAdmin = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during admin rights search");
            System.err.println(e.getMessage());
        }

        return userIsAdmin;
    }

    /**
     * Check for admin setting in config
     * @return isAdmin flag
     */
    public static boolean checkForAdminRightsInConfig(String user_name){
        boolean userIsAdmin = false;

        ArrayList<String> admins = YambApp.getConfig().getStringArrayListParameter("main", "admin_names");

        if(admins.contains(user_name)){
            userIsAdmin = true;
        }

        return userIsAdmin;
    }
}
