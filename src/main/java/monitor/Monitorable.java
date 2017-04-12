package monitor;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 */
public interface Monitorable {
    void addListener(MonitorObserver monitorObserver);
    void removeListener(MonitorObserver monitorObserver);

    /**
     * Notify all observers about the event
     */
    void notifyObservers();

    void runMonitor();
}
