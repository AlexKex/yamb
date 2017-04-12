package monitoringtask;

import org.json.JSONObject;

/**
 * Created by Alex Pryakhin on 12.04.2017.
 */
public class MysqlMonitoringTask extends MonitoringTask{
    public MysqlMonitoringTask(JSONObject parameters) {
        super(parameters);
        System.out.println(123);
    }

    @Override
    public void run() {

    }
}
