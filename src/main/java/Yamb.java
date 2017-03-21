import yamb.YambApp;

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

        YambApp app = new YambApp(configurationDirectory);
        app.startApp();
        while(app.isAppRunning()){
            if(app.getBot().hasSubscribers()){
                app.getBot().sendBrodcastMessage("Hello");
            }
        }
    }
}
