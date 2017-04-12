package monitor;

import monitoringtask.MonitoringTask;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alex Pryakhin on 09.03.2017.
 *
 * All monitor types should extend this abstract monitor
 */
public class Monitor implements Monitorable {
    protected ArrayList<MonitorObserver> monitorObservers = new ArrayList<MonitorObserver>();
    protected MonitorEvent event;
    protected JSONObject monitorParameters;
    protected JSONObject monitorTaskParameters;
    protected int monitoringInterval;
    protected String monitorType;
    protected String monitorName;

    public Monitor(JSONObject parameters){
        monitorParameters = parameters;

        monitorName = monitorParameters.getString("monitor_name");
        monitorType = monitorParameters.getString("monitor_type");
        monitoringInterval = monitorParameters.getInt("monitor_interval");

        monitorTaskParameters = monitorParameters.getJSONObject("monitor_parameters");
    }

    /**
     * Adds listener (observer) to monitor
     * @param monitorObserver - observer object
     */
    @Override
    public void addListener(MonitorObserver monitorObserver) {
        monitorObservers.add(monitorObserver);
    }

    /**
     * Removes listener
     * @param monitorObserver - observer object
     */
    @Override
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

    @Override
    public void runMonitor() {
        Timer monitorTimer = new Timer();
        String taskName = monitorType + "MonitoringTask";

        try{
            monitorTimer.schedule((MonitoringTask) Class.forName(taskName).getConstructor(MonitoringTask.class).newInstance(), monitoringInterval);
        }
        catch (ClassNotFoundException e){
            System.err.println("Class " + taskName + " wasn't found");
            System.err.println(e.getMessage());
        }
        catch (InstantiationException e){
            System.err.println("InstantiationException with class " + taskName);
            System.err.println(e.getMessage());
        }
        catch (NoSuchMethodException e) {
            System.err.println("No such method in class " + taskName);
            System.err.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access to class " + taskName);
            System.err.println(e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.println("Invocation target error with " + taskName);
            System.err.println(e.getMessage());
        }
    }
}
