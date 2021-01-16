package Model;

import Interfaces.MenuSetup;
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
    MenuSetup setupHandler;
    /**
     * Constructor for the update listener
     * @param channelHandler The channelHandler object for the channels
     * @param tableEditor The table editor object for handling the table
     * @param gui The GUI object
     * @param setupHandler the interface to update the dropdown menu
     */
    public UpdateListener(ChannelHandler channelHandler,
                          TableInterface tableEditor, GUI gui,
                          MenuSetup setupHandler){
        this.channelHandler = channelHandler;
        this.tableEditor = tableEditor;
        this.gui = gui;
        this.setupHandler = setupHandler;
    }

    /**
     * Actionperformed method to execute the update upon clicking the update
     * button. Reloads the channels and episodes from the API and updates the
     * table accordingly
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        gui.getUpdateButton().setEnabled(false);
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                channelHandler.loadChannels();
                return null;
            }

            @Override
            protected void done() {
                String defaultChannel = "P1";
                tableEditor.updateTable(defaultChannel);
                setupHandler.setupDropDown();
                gui.getUpdateButton().setEnabled(true);
                super.done();
            }
        };
        worker.execute();
    }
}