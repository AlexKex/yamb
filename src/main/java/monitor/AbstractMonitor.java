package monitor;

import java.util.ArrayList;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 *
 * All monitor types should extend this abstract monitor
 */
public abstract class AbstractMonitor implements Monitor {
    protected ArrayList<MonitorObserver> monitorObservers = new ArrayList<MonitorObserver>();
    protected MonitorEvent event;

    /**
     * Adds listener (observer) to monitor
     * @param monitorObserver - observer object
     */
    public void addListener(MonitorObserver monitorObserver) {
        monitorObservers.add(monitorObserver);
    }

    /**
     * Removes listener
     * @param monitorObserver - observer object
     */
    public void removeListener(MonitorObserver monitorObserver) {
        monitorObservers.add(monitorObserver);
    }

    /**
     * Notifies all existing observers via Telegram
     */
    public void notifyObservers() {
        for(MonitorObserver mo : monitorObservers){
            mo.update(this, event);
        }
    }
}
