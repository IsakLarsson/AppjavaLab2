import Model.*;
import View.GUI;
import javax.swing.*;
import Interfaces.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.concurrent.*;

/**
 * Controller class for handling communication between model and view
 */
public class Controller {
    private HashMap<String, Channel> channelMap;
    GUI gui;
    Object lock;
    TreeMap<String, Channel> sorted = new TreeMap<>(); //will sort the map

    /**
     * Setuphandler implementing the MenuSetup interface so that it can be 
     * executed in other classes without creating too strong dependencies
     */
    public class MenuSetupHandler implements MenuSetup{ 
        public void setupDropDown(){
            gui.clearDropDown();
            int index = 0;
            sorted.clear();
            sorted.putAll(channelMap);
            for (String channelName : sorted.keySet()){
                try {
                    
                    gui.insertComboBoxItem(channelName, index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                index++;
            }
        }
    }

    /**
     * Constructor for the Controller class, starts the EDT and starts up 
     * the model
     * @throws InterruptedException
     */
    public Controller() throws InterruptedException {
        channelMap = new HashMap();
        MenuSetup menuSetupHandler = new MenuSetupHandler();
        

        SwingUtilities.invokeLater(() -> {
            gui = new GUI("Radio Info");
            TableInterface tableEditor = new TableEditor(channelMap, gui);
            ChannelHandler channelHandler = new ChannelHandler(channelMap, 
                menuSetupHandler, gui.getUpdateButton(), tableEditor);
            
                gui.setupListeners(new ComboListener(tableEditor, gui),
                    new TableListener(gui, channelMap), 
                    new UpdateListener(channelHandler, tableEditor, gui));
            gui.show();
            
            channelHandler.start();
            
        });
       
    }

}
