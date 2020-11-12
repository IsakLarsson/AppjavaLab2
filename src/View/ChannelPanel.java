package View;

import javax.swing.*;
import java.awt.*;

public class ChannelPanel extends JPanel {
    JComboBox channelSelector;

    public ChannelPanel(){
        channelSelector = new JComboBox();
        setLayout(new BorderLayout());
        add(channelSelector, BorderLayout.CENTER);
        channelSelector.insertItemAt("Loading...", 0);
    }

    public void insertItem(String channelName, int index){
        if(channelSelector.getItemAt(0).equals("Loading...")){
            channelSelector.removeAllItems();
        }
        channelSelector.insertItemAt(channelName, index);
    }

    public JComboBox getChannelSelector() {
        return channelSelector;
    }
}
