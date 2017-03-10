package monitor;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 */
public interface MonitorObserver {
    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param   observableMonitor     the observable monitor.monitor object.
     * @param   arguments             an argument passed to the <code>notifyObservers</code>
     *                                method.
     */
    void update(Monitor observableMonitor, Object arguments);
}
