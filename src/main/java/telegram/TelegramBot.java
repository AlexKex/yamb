package telegram;

import database.DatabaseHandler;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Extended bot functionality
 * subscribers - map for subscribers storage in format user_id -> chat_id
 * Created by Alex Pryakhin on 10.03.2017.
 */
public class TelegramBot extends TelegramLongPollingBot {
    private String botName;
    private String botKey;
    private HashMap<String, Long> subscribers = new HashMap<String, Long>();

    public TelegramBot(String name, String key){
        botName = name;
        botKey = key;
    }

    public void startBot(){
        try{
            TelegramBotsApi botsApi = new TelegramBotsApi();
            botsApi.registerBot(this);
        }
        catch(TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * return or restore subscribers list
     */
    public HashMap<String, Long>  getSubscribers(){
        if(subscribers.size() == 0){
            restoreSubscribers();
        }

        return subscribers;
    }

    /**
     * restore subscribers list from SQLite DB
     */
    private void restoreSubscribers() {
        String query = "SELECT * FROM subscribers";

        try {
            ResultSet resSet = DatabaseHandler.getQueryResult(query);

            while(resSet.next())
            {
                subscribers.put(resSet.getString("user_name"), resSet.getLong("id"));
            }
        }
        catch (SQLException e){
            System.err.println("Unable to restore subscribers");
            System.err.println(e.getMessage());
        }
    }

    /**
     * incoming message receiver
     * @param update - message object
     */
    public void onUpdateReceived(Update update) {
        if(!subscribers.containsKey(update.getMessage().getFrom().getUserName())){
            addSubscriber(update.getMessage().getFrom().getUserName(), update.getMessage().getChatId());
        }
    }

    /**
     * Add subscriber to the current subscribers list and to SQLite DB
     * @param user_name - Telegram subscriber user name
     * @param user_id - Telegram subscriber ID
     */
    public void addSubscriber(String user_name, Long user_id){
        if(!subscribers.containsKey(user_name)){
            subscribers.put(user_name, user_id);
        }

        try {
            String query = "INSERT INTO subscribers VALUES (";
            query += user_id + ",";
            query += "'" + user_name + "')";
            DatabaseHandler.executeQuery(query);
        }
        catch (SQLException e){
            System.err.println("Can's save subscriber to the DB");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checks if subscribers list not empty
     * @return true or false
     */
    public boolean hasSubscribers(){
        return subscribers.size() > 0;
    }

    public String getBotUsername() {
        return this.botName;
    }

    public String getBotToken() {
        return this.botKey;
    }

    /**
     * Send message to whole subscribers list
     * @param message - message string
     */
    public void sendBrodcastMessage(String message){
        Integer messageCount = 0;

        for(Map.Entry<String, Long> subscribberEntry : subscribers.entrySet()){
            SendMessage sm = new SendMessage();
            sm.setChatId(subscribberEntry.getValue());
            sm.setText(message);

            try{
                sendMessage(sm);
                messageCount++;

                // sleep every 25 messages due to Telegram requirements
                if(messageCount % 25 == 0){
                    wait(2000);
                }
            }
            catch (TelegramApiException e){
                System.err.println("Telegram API error while sending message");
                System.err.println(e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Thread error while sending message");
                System.err.println(e.getMessage());
            }
        }
    }
}
