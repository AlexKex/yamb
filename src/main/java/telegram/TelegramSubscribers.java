package telegram;

import java.util.HashMap;

/**
 * Created by Alex Pryakhin on 14.03.2017.
 */
public class TelegramSubscribers {
    private HashMap<String, Long> subscribers = new HashMap<String, Long>();

    public TelegramSubscribers(){

    }

    public HashMap<String, Long> getSubscribers(){
        return subscribers;
    }

    public void addSubscriber(String user_name, Long user_id){
        if(!subscribers.containsKey(user_name)){
            subscribers.put(user_name, user_id);
        }
    }
}
