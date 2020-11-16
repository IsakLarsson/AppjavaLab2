package Model;

import View.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Actionlistener class for reloading and updating the data in the
 * table
 */
public class UpdateListener implements ActionListener {
    ChannelHandler channelHandler;
    TableInterface tableEditor;
    GUI gui;

    /**
     * Constructor for the update listener
     * @param channelHandler The channelHandler object for the channels
     * @param tableEditor The table editor object for handling the table
     * @param gui The GUI object
     */
    public UpdateListener(ChannelHandler channelHandler, TableInterface tableEditor, GUI gui){
        this.channelHandler = channelHandler;
        this.tableEditor = tableEditor;
        this.gui = gui;
    }

    /**
     * Actionperformed method to execute the update upon clicking the update
     * button. Reloads the channels and episodes from the API and updates the
     * table accordingly
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                Object dropDownChoice = gui.getSelectedValue();
                channelHandler.loadChannels();
                tableEditor.updateTable(dropDownChoice.toString());
                return null;
            }
        };
        worker.execute();
    }
}