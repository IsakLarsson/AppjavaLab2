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
        if(channelSelector.getItemAt(0) == null){
            channelSelector.insertItemAt(channelName, index);
            return;
        } else if(channelSelector.getItemAt(0).equals("Loading...")){
            channelSelector.removeItemAt(0);
        }
        channelSelector.insertItemAt(channelName, index);
    }

    public JComboBox getChannelSelector() {
        return channelSelector;
    }

    public void clearDropDown(){
        channelSelector.removeAllItems();
    }
}
