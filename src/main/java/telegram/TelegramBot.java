package telegram;

import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

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
     * incoming message receiver
     * @param update - message object
     */
    public void onUpdateReceived(Update update) {
        if(!subscribers.containsKey(update.getMessage().getFrom().getUserName())){
            subscribers.put(update.getMessage().getFrom().getUserName(), update.getMessage().getChatId());
        }
    }

    public boolean hasSubscribers(){
        return subscribers.size() > 0;
    }

    public String getBotUsername() {
        return this.botName;
    }

    public String getBotToken() {
        return this.botKey;
    }

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
