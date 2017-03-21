package yamb;

import database.DatabaseHandler;
import org.telegram.telegrambots.ApiContextInitializer;
import telegram.TelegramBot;

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
    }

    public static YambConfig getConfig(){
        return config;
    }
}
