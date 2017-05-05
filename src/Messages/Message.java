package Messages;

import java.io.Serializable;

/**
 * Created by ChristianBuskirk on 5/4/17.
 */
public class Message implements Serializable {
    // These are public because this whole class is designed as a messenger between other classes
    public String action;
    public int target;
    public int source;
    public int value;

    public Message(String actionstring) {
        action = actionstring;
    }
}
