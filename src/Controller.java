import Model.*;
import View.GUI;

import javax.swing.*;

import Interfaces.MenuSetup;

import java.util.*;
import java.util.function.Consumer;
import java.util.concurrent.*;

public class Controller {
    private HashMap<String, Channel> channelMap;
    GUI gui;
    Object lock;

    public class MenuSetupHandler implements MenuSetup{ //Interface för att kunna skicka in och exekveras i annnan tråd utan att bryta enkapsuleringen
        public void setup(){
            int index = 0;
            TreeMap<String, Channel> sorted = new TreeMap<>(); //sorts the map
            sorted.putAll(channelMap);
            for (String channelName : sorted.keySet()){
                gui.insertComboBoxItem(channelName, index);
                index++;
            }
        }
    }

    public Controller() throws InterruptedException {
        channelMap = new HashMap();
        MenuSetupHandler menuSetupHandler = new MenuSetupHandler();
        

        SwingUtilities.invokeLater(() -> {
            gui = new GUI("SvEriGeS RadIO");
            ChannelHandler channelHandler = new ChannelHandler(channelMap, menuSetupHandler, gui.getUpdateItem());
            TableInterface tableEditor = new TableEditor(channelMap, gui);
            gui.setupListeners(new ComboListener(tableEditor, gui),
                    new TableListener(gui, channelMap), new UpdateListener(channelHandler));
            gui.show();

            channelHandler.start();
            
        });
       
        

        //TODO fixa uppdatering på demand, trådskit
        //Thread.sleep(5000); //channelmappen verkar inte hinna initas innan den ska uppdateras, trådproblem HERES THE PROBLEM YEA BUDDY
        //setupChoices();
    }

    // public void setupChoices(){
    //     int index = 0;
    //     TreeMap<String, Channel> sorted = new TreeMap<>(); //sorts the map
    //     sorted.putAll(channelMap);
    //     for (String channelName : sorted.keySet()){
    //         gui.insertComboBoxItem(channelName, index);
    //         index++;
    //     }
    // }

}
