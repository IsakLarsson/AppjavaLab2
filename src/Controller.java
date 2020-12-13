import Model.*;
import View.GUI;
import javax.swing.*;
import Interfaces.*;
import java.util.*;

/**
 * Controller class for handling communication between model and view
 */
public class Controller {
    private HashMap<String, Channel> channelMap;
    private TreeMap<String, Channel> sorted = new TreeMap<>(); //will sort the map
    GUI gui;

    /**
     * Setuphandler implementing the MenuSetup interface so that it can be 
     * executed in other classes without creating too strong dependencies
     */
    public class MenuSetupHandler implements MenuSetup{ 
        public synchronized void setupDropDown(){
            gui.clearDropDown();
            int index = 0;
            sorted.clear();
            sorted.putAll(channelMap);
            for (String channelName : sorted.keySet()){
                try {
                    gui.insertComboBoxItem(channelName, index);
                } catch (Exception e) {
                    gui.showPopUp("Could not add channel");
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
            
            channelHandler.load();
            
        });
       
    }

}
