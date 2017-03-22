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
    private static HashMap<Long, TelegramSubscriber> subscribersList = new HashMap<Long, TelegramSubscriber>();
    private static boolean isOpenChannel;
    private static String customChannelName;

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
    public HashMap<Long, TelegramSubscriber> getSubscribers(){
        if(!hasSubscribers()){
            restoreSubscribers();
        }

        return subscribersList;
    }

    public static Integer getSubscribersListSize(){
        return subscribersList.size();
    }

    /**
     * restore subscribers list from SQLite DB
     */
    private void restoreSubscribers() {
        subscribersList = TelegramSubscriber.restoreSubscribersList();
    }

    /**
     * incoming message receiver
     * @param update - message object
     */
    public void onUpdateReceived(Update update) {
        TelegramUser user = new TelegramUser(update);

        doMessageAction(update, user);
    }

    /**
     * Add subscriber to the current subscribers list and to SQLite DB
     * @param userName - Telegram subscriber user name
     * @param userId - Telegram subscriber ID
     * @param chatId - Telegram subscriber chat ID
     */
    public boolean addSubscriber(String userName, Long userId, Long chatId){
        boolean result = false;

        if(!subscribersListContainsSubscriber(userId)) {
            TelegramSubscriber newTS = new TelegramSubscriber(userId, userName, chatId);
            subscribersList.put(userId, newTS);
            newTS.saveSubscriberToDB();
            result = true;
        }

        return result;
    }

    /**
     * Removes subscriber from subscribers collection and from DB
     * @param userId - Telegram user id
     * @return boolean flag of success
     */
    public boolean removeSubscriber(Long userId){
        boolean result = false;

        if(subscribersListContainsSubscriber(userId)){
            TelegramSubscriber removeTS = subscribersList.get(userId);
            subscribersList.remove(userId);
            removeTS.removeSubscriberFromDB();
            result = true;
        }

        return result;
    }

    /**
     * Checks if subscribers list not empty
     * @return true or false
     */
    public boolean hasSubscribers(){
        return subscribersList.size() > 0;
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

        SendMessage broadcastMessage = new SendMessage();

        for(Map.Entry<Long, TelegramSubscriber> subscribberEntry : subscribersList.entrySet()){
            broadcastMessage.setChatId(subscribberEntry.getValue().getChatId());
            broadcastMessage.setText(message);

            try{
                sendMessage(broadcastMessage);
                messageCount++;

                // sleep every 25 messages due to Telegram requirements
                if(messageCount % 25 == 0){
                    Thread.sleep(2000);
                }
            }
            catch (TelegramApiException e){
                System.err.println("Telegram API error while sending broadcast message");
                System.err.println(e.getMessage());
            } catch (InterruptedException e) {
                System.err.println("Thread error while sending message");
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Sends personal message to user
     * @param message - String, message text
     * @param update - Telegram incoming message object
     */
    public void sendSingleMessage(String message, Update update){
        SendMessage singleMessage = new SendMessage();
        singleMessage.setChatId(update.getMessage().getChatId());
        singleMessage.setText(message);
        try {
            sendMessage(singleMessage);
        } catch (TelegramApiException e) {
            System.err.println("Telegram API error while sending single message");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends personal message to user
     * @param message - String, message text
     * @param chatId - Long Telegram chatid
     */
    public void sendSingleMessage(String message, Long chatId){
        SendMessage singleMessage = new SendMessage();
        singleMessage.setChatId(chatId);
        singleMessage.setText(message);
        try {
            sendMessage(singleMessage);
        } catch (TelegramApiException e) {
            System.err.println("Telegram API error while sending single message");
            System.err.println(e.getMessage());
        }
    }

    public void sendAdminNotifier(String message) {
        Integer messageCounter = 0;

        try {
            for (Map.Entry<Long, TelegramUser> adminEntry : TelegramUser.getAdminsList().entrySet()) {
                sendSingleMessage(message, adminEntry.getValue().getChatId());

                if (messageCounter % 30 == 0)
                    Thread.sleep(2000);

                messageCounter++;
            }
        }
        catch (InterruptedException e){
            System.err.println("Error with admin sendout");
            System.err.println(e.getMessage());
        }
    }

    /**
     * setter for user configurated channel name
     * @param configCustomChannelName custom channel name, coulb empty string
     */
    public void setBotCustomChannelName(String configCustomChannelName){
        if(configCustomChannelName.equals(""))
            customChannelName = getBotUsername();
        else
            customChannelName = configCustomChannelName;
    }

    /**
     * setter for channel accessibility
     * @param configChannelAccessibility - boolean open flag
     */
    public void setChannelAccessibility(boolean configChannelAccessibility){
        isOpenChannel = configChannelAccessibility;
    }

    public boolean userIsSubscribed(Long user_id){
        return subscribersList.containsKey(user_id);
    }

    /**
     * action controller
     * @param update - Telegram update object
     */
    private void doMessageAction(Update update, TelegramUser user){
        String message_text = update.getMessage().getText();

        /* simple commands */
        if(message_text.equals("/start")){
            // prints welcome message
            if (isOpenChannel) {
                String welcomeMessage = "Hello, " + user.getUserName() + "! ";
                welcomeMessage += "Welcome to " + customChannelName;

                sendSingleMessage(welcomeMessage, update);
            } else {
                 String senderNotification = "It's private channel. Your ID will be send to channel's moderator";
                 sendSingleMessage(senderNotification, update);
            }
        }
        else if(message_text.equals("/help")){
            // man page
            sendSingleMessage(TelegramBotMessageHandler.prepareHelp(), update);
        }
        else if(message_text.equals("/sub")){
            // subscription logic
            if(TelegramUser.checkForAdminRightsInConfig(update.getMessage().getFrom().getUserName())){
                // add admin to users table
                TelegramUser admin = new TelegramUser(
                        update.getMessage().getFrom().getUserName(),
                        update.getMessage().getFrom().getId().longValue(),
                        update.getMessage().getChatId(),
                        true
                );
                if(!TelegramUser.getAdminsList().containsKey(admin.getUserId())){
                    admin.register();
                }

                // greetings to admin
                sendSingleMessage(TelegramBotMessageHandler.prepareAdminGreetings(), update);
            }
            else{
                if(isOpenChannel) {
                    //subscribe everybody
                    if(userIsSubscribed(update.getMessage().getFrom().getId().longValue())){
                        sendSingleMessage(TelegramBotMessageHandler.prepareSubscriptionMessage(), update);
                    }
                    else{
                        if (addSubscriber(update.getMessage().getFrom().getUserName(), update.getMessage().getFrom().getId().longValue(), update.getMessage().getChatId()))
                            sendSingleMessage(TelegramBotMessageHandler.prepareSubscriptionMessage(true), update);
                        else
                            sendSingleMessage(TelegramBotMessageHandler.prepareSubscriptionMessage(false), update);
                    }
                }
                else{
                    // if it's new admin
                    sendSingleMessage(TelegramBotMessageHandler.prepareWaitingMessage(), update);
                    sendAdminNotifier(TelegramBotMessageHandler.prepareAdminSubstriptionMessage(update));
                }
            }
        }
        else if(message_text.equals("/unsub")){
            if(removeSubscriber(update.getMessage().getFrom().getId().longValue()))
                sendSingleMessage(TelegramBotMessageHandler.prepareUnsubscriptionMessage(true), update);
            else
                sendSingleMessage(TelegramBotMessageHandler.prepareUnsubscriptionMessage(false), update);
        }
        /* combined comands */
        else{
            String[] message_parts = message_text.split(" ");
        }
    }

    /**
     * Check for existing user in user list by telegram user id
     * @param userId - needle user id
     * @return exist flag
     */
    private boolean subscribersListContainsSubscriber(Long userId){
        return subscribersList.containsKey(userId);
    }
}
