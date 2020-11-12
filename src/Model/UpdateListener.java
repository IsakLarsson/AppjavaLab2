package Model;

import View.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateListener implements ActionListener {
    ChannelHandler channelHandler;

    public UpdateListener(ChannelHandler channelHandler){
        this.channelHandler = channelHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                channelHandler.loadChannels();
                //kalla på channelhandlers update
                return null;
            }
        };
        worker.execute();
    }
}