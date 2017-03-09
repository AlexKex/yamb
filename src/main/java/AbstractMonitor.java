import java.util.ArrayList;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 */
public abstract class AbstractMonitor implements Monitor {
    protected ArrayList<MonitorObserver> monitorObservers = new ArrayList<MonitorObserver>();

    public void addListener(MonitorObserver monitorObserver) {

    }

    public void removeListener(MonitorObserver monitorObserver) {

    }

    public void notifyObservers() {

    }
}
