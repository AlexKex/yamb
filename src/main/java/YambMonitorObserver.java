import monitor.Monitor;
import monitor.MonitorObserver;
import telegram.TelegramBot;

/**
 * Created by Alex Pryakhin on 10.03.2017.
 */
public class YambMonitorObserver implements MonitorObserver {
    private TelegramBot informerBot;

    public YambMonitorObserver(TelegramBot bot){
        informerBot = bot;
    }

    public void update(Monitor observableMonitor, Object arguments) {

    }
}
