package monitor;

import javafx.beans.Observable;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 */
public interface Monitor extends Observable {
    void addListener(MonitorObserver monitorObserver);
    void removeListener(MonitorObserver monitorObserver);

    /**
     * Notify all observers about the event
     */
    void notifyObservers();
}