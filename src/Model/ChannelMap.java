package Model;

import java.util.HashMap;

/**
 * Class for holding channels
 */
public class ChannelMap {
    public HashMap<String, Channel> channels;
    public boolean loading;

    public ChannelMap() {
        channels = new HashMap();
        loading = false;
    }
}
