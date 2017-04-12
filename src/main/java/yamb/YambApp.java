package yamb;

import database.DatabaseHandler;
import org.telegram.telegrambots.ApiContextInitializer;
import telegram.TelegramBot;
import telegram.TelegramBotMessageHandler;
import telegram.TelegramSubscriber;
import telegram.TelegramUser;

/**
 * Created by Alex Pryakhin on 21.03.2017.
 */
public class YambApp {
    private TelegramBot bot;
    private static DatabaseHandler db;
    private YambMonitorObserver observer;
    private static YambConfig config;
    private boolean isRunning = false;

    public YambApp(String configurationDirectory){
        // read main configuration file
        config = new YambConfig(configurationDirectory);

        DatabaseHandler.initDatabase();
        TelegramUser.initAdmins();

        // read the list of monitors
        this.createMonitors();

        // create bot
        // Initializing Telegram bot environment
        ApiContextInitializer.init();
        bot = new TelegramBot(
                config.getStringParameter("telegram", "bot_name"),
                config.getStringParameter("telegram", "bot_token")
        );

        bot.setBotCustomChannelName(config.getStringParameter("main", "custom_channel_name"));
        bot.setChannelAccessibility(config.getBooleanParameter("main", "is_open_channel"));
        bot.startBot();
        bot.getSubscribers();

        // create system observer
        observer = new YambMonitorObserver(bot);
    }

    public void startApp(){
        this.isRunning = true;

        String message = TelegramBotMessageHandler.restartBotMessage() + "\n";
        message += "Admins registered:      " + TelegramUser.getAdminsList().size() + "\n";
        message += "Subscribers registered: " + TelegramBot.getSubscribersListSize();
        bot.sendAdminNotifier(message);
    }

    public void stopApp(){
        this.isRunning = false;
    }

    /**
     * checks if the app is running
     * @return boolean
     */
    public boolean isAppRunning(){
        return isRunning;
    }

    /**
     * TelegramBot getter
     * @return TelegramBot
     */
    public TelegramBot getBot(){
        return bot;
    }

    private void createMonitors() {
        // TODO !!!
        // read monitors configuration

        // add monitor for each config

    }

    public static YambConfig getConfig(){
        return config;
    }
}
