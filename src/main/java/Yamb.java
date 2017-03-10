import telegram.TelegramBot;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 *
 * Application is monitoring events, specified in configuration files.
 * There are several monitor types with it's own behaviour.
 *
 * Concrete behaviour can be declarative specified in configuration file.
 * All configuration file syntax described in concrete monitor files classes.
 */
public class Yamb {
    private TelegramBot bot;
    private YambMonitorObserver observer;
    private YambConfig config;

    public Yamb(String configurationDirectory){
        // read main configuration file
        config = new YambConfig(configurationDirectory);

        // read the list of monitors
        this.createMonitors();

        // create system observer
        observer = new YambMonitorObserver();
        bot = new TelegramBot();
    }

    /**
     * @param args - launch parameters
     * -c - path to configuration directory
     *             -c=/path/to/config/directory/
     */
    public static void main(String[] args){
        String configurationDirectory = "";

        for(String arg: args){
            String[] argParts = arg.split("=");
            String key = argParts[0];
            String value;

            if(argParts.length > 1){
                value = argParts[1];
            }
            else{
                value = null;
            }

            if(key.equals("-c")){
                configurationDirectory = value;
            }
        }


        Yamb app = new Yamb(configurationDirectory);
    }

    private void createMonitors() {
    }
}
