import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class for work with JSON configurations
 * Can read main configuration & monitors' configurations
 */
public class YambConfig {
    private static String mainConfigName = "yamb.conf";
    private static String monitorConfigPostfix = ".mon.conf";

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
     * String getter for config parameters
     * @param parameterName
     * @return
     */
    public String getStringParameter(String parameterName){
        return "";
    }

    /**
     * Integer getter for config parameters
     * @param parameterName
     * @return
     */
    public Integer getIntegerParameter(String parameterName){
        return null;
    }

    private void readCofig(File configurationFile) throws IOException{
        FileReader configurationFileReader = new FileReader(configurationFile.getAbsolutePath());
        JSONTokener jsonTokener = new JSONTokener(configurationFileReader);
        JSONObject configJsonObject = new JSONObject(jsonTokener);

        // TODO create read logic
    }
}
