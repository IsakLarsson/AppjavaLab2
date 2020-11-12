import Model.*;
import View.GUI;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;

public class Controller {
    private HashMap<String, Channel> channelMap;
    GUI gui;

    public Controller() throws InterruptedException {
        channelMap = new HashMap();

        SwingUtilities.invokeLater(() -> {
            gui = new GUI("SvEriGeS RadIO");
            TableInterface tableEditor = new TableEditor(channelMap, gui);
            gui.setupListeners(new ComboListener(tableEditor, gui),
                    new TableListener(gui, channelMap));
            gui.show();
            ChannelHandler channelHandler = new ChannelHandler(channelMap);
            channelHandler.start();
        });

        //TODO fixa uppdatering på demand, trådskit
        //Thread.sleep(1000);
        setupChoices();
    }

    public void setupChoices(){
        int index = 0;
        TreeMap<String, Channel> sorted = new TreeMap<>(); //sorts the map
        sorted.putAll(channelMap);
        for (String channelName : sorted.keySet()){
            gui.insertComboBoxItem(channelName, index);
            index++;
        }
    }

}
