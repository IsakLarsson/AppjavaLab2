package View;

import javax.swing.*;
import java.awt.*;

/**
 * JPanel class for displaying the different programs
 */
public class ChannelPanel extends JPanel {
    JComboBox channelSelector;

    /**
     * Constructor for the channel panel, sets the layout for the panel
     */
    public ChannelPanel(){
        channelSelector = new JComboBox();
        setLayout(new BorderLayout());
        add(channelSelector, BorderLayout.CENTER);
        channelSelector.insertItemAt("Loading...", 0);
    }

    /**
     * Inserts an item into the channel selector combo box (dropdown)
     * @param channelName The name of the channel
     * @param index The index to add the item at
     */
    public void insertItem(String channelName, int index){
        if(channelSelector.getItemAt(0) == null){
            channelSelector.insertItemAt(channelName, index);
            return;
        } else if(channelSelector.getItemAt(0).equals("Loading...")){
            channelSelector.removeItemAt(0);
        }
        channelSelector.insertItemAt(channelName, index);
    }

    /**
     * Getter for the channel selector combobox
     */
    public JComboBox getChannelSelector() {
        return channelSelector;
    }

    /**
     * Clears the combobox
     */
    public void clearDropDown(){
        channelSelector.removeAllItems();
    }
}
