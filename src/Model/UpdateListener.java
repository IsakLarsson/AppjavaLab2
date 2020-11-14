package Model;

import View.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateListener implements ActionListener {
    ChannelHandler channelHandler;
    TableInterface tableEditor;
    GUI gui;

    public UpdateListener(ChannelHandler channelHandler, TableInterface tableEditor, GUI gui){
        this.channelHandler = channelHandler;
        this.tableEditor = tableEditor;
        this.gui = gui;
    }

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