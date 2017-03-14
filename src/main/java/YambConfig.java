import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class for work with JSON configurations
 * Can read main configuration & monitors' configurations
 */
public class YambConfig {
    private static String mainConfigName = "yamb.conf";
    private static String monitorConfigPostfix = ".mon.conf";

    private Map<String, JSONObject> configuration = new HashMap<String, JSONObject>();
    private Map<String, JSONObject> monitorsConfiguration = new HashMap<String, JSONObject>();

    public YambConfig(String configurationDirectory){
        // read config dir
        try{
            File configDir = new File(configurationDirectory);
            File[] fs = configDir.listFiles();

            if(fs != null){
                for(File fileEntry : fs){
                    readCofig(fileEntry);
                }
            }
        }
        catch (IOException e){
            System.err.println("IOException during configuration read");
            System.err.println(e.getMessage());
        }
    }

    /**
     * String getter for main app config parameters
     * @param parameterName - get parameter by name and block
     * @return
     */
    public String getMainStringParameter(String parameterBlock, String parameterName){
        String parameterValue = "";

        try{
            JSONObject parameters = configuration.get(parameterBlock);

            parameterValue = parameters.getString(parameterName);
        }
        catch(Exception e){
            System.err.println("Error while reading main configuration on block " + parameterBlock + " with parameter " + parameterName);
        }

        return parameterValue;
    }

    /**
     * Integer getter for config parameters
     * @param parameterName- get parameter by name and block
     * @return
     */
    public Integer getMainIntegerParameter(String parameterName, String parameterBlock){
        Integer parameterValue = null;

        try{
            JSONObject parameters = configuration.get(parameterBlock);

            parameterValue = parameters.getInt(parameterName);
        }
        catch(Exception e){
            System.err.println("Error while reading main configuration on block " + parameterBlock + " with parameter " + parameterName);
        }

        return parameterValue;
    }

    private void readCofig(File configurationFile) throws IOException{
        FileReader configurationFileReader = new FileReader(configurationFile.getAbsolutePath());
        JSONTokener jsonTokener = new JSONTokener(configurationFileReader);
        JSONObject configJsonObject = new JSONObject(jsonTokener);

        if(configurationFile.getName().equals(mainConfigName)){
            // main configuration
            Iterator<String> configIterator = configJsonObject.keys();
            while(configIterator.hasNext()){
                String current_key = configIterator.next();
                configuration.put(current_key, configJsonObject.getJSONObject(current_key));
            }
        }
        else{
            // monitor configuration
            monitorsConfiguration.put(
                    configJsonObject.getString("monitor_name"),
                    configJsonObject
            );
        }
    }
}
