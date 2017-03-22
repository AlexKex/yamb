package telegram;

import org.telegram.telegrambots.api.objects.Update;

/**
 * Created by Alex Pryakhin on 21.03.2017.
 */
public class TelegramBotMessageHandler
{
    /**
     * Prepare help message
     * @return message
     */
    public static String prepareHelp(){
        String help = "";

        help += "List of available commands \n";
        help += "/sub - request a subscribe to monitor \n";
        help += "/unsub - unsubscribe \n";
        help += "/list - show active monitors [under construction] \n";

        return help;
    }

    /**
     * Message on unsubscribe event
     * @param success - flag
     * @return message
     */
    static String prepareSubscriptionMessage(boolean success){
        return success ? "You've been successfully subscribed to the channel" : "Something goes wrong, but probably you will receive messages. Probably...";
    }

    static String prepareSubscriptionMessage(){
        return "You're already subscribed";
    }

    /**
     * Warning for private monitors
     * @return message
     */
    static String prepareWaitingMessage(){
        return "It's private channel. Your request will proceed to channel moderator.";
    }

    /**
     * Message on unsubscribe event
     * @param success - flag
     * @return message
     */
    static String prepareUnsubscriptionMessage(boolean success){
        return success ? "You've been successfully unsubscribed" : "Something goes wrong";
    }

    static String prepareAdminSubstriptionMessage(Update update){
        return "User " + update.getMessage().getFrom().getUserName() + " asked for channel access";
    }

    static String prepareAdminGreetings(){
        return "Hail to the king!";
    }

    public static String restartBotMessage(){
        return "Bot has been restarted";
    }
}
