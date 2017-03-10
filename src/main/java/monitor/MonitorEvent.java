package monitor;

/**
 * Created by Alex Pryakhin on 10.03.2017.
 */
public abstract class MonitorEvent {
    String message;
    enum message_type{
        CRITICAL,       // system fail
        URGENT,         // risk of system fail
        WARNING,        // data or system error
        NOTIFICATION,   // any non-critical error
        INFO            // information message
    };

}
