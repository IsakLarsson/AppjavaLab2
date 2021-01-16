package View;

import Model.ComboListener;
import Model.Episode;
import Model.TableListener;
import Model.UpdateListener;

import javax.swing.*;
import java.awt.*;

/**
 * The GUI class to display all the graphical components of the program
 */
public class GUI {
    private JFrame frame;
    private int size = 400;
    private JMenuItem updateItem;
    private JMenuItem aboutItem;

    ChannelPanel channelPanel;
    ProgramPanel programpPanel;

    /**
     * GUI constructor, sets all the relevant values and builds the separate
     * panels into the frame
     * @param title The title of the program to be displayed
     */
    public GUI(String title){
        channelPanel = buildUpperPanel();
        programpPanel = buildCenterPanel();
        frame = new JFrame(title);

        frame.setJMenuBar(buildMenuBar());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(size,size);
        frame.setLocationRelativeTo(null);
        frame.add(channelPanel, BorderLayout.NORTH);
        frame.add(programpPanel, BorderLayout.CENTER);
        frame.pack();
    }

    /**
     * Shows the gui
     */
    public void show() {
        frame.setVisible(true);
    }


    /**
     * Builds the menu bar that contains the menu choices like "file"
     * @return The menu bar object
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        updateItem = new JMenuItem("Uppdatera");

        fileMenu.add(updateItem);

        //JMenu helpMenu = new JMenu("Help");
        
        menuBar.add(fileMenu);
        //menuBar.add(helpMenu);

        return menuBar;
    }


    /**
     * Sets up the actionlisteners for the comboBox (dropdown menu),
     * update button and program table.
     * @param comboListener Listener for the combobox
     * @param tableListener Listener for the table
     * @param updateListener Listener for the update button
     */
    public void setupListeners(ComboListener comboListener,
                               TableListener tableListener,
                               UpdateListener updateListener){
        channelPanel.getChannelSelector().addActionListener(comboListener);
        programpPanel.getTable().addMouseListener(tableListener);
        updateItem.addActionListener(updateListener);
    }


    /**
     * Builds the upper panel of the frame, containing a ChannelPanel object
     * @return The created ChannelPanel object
     */
    private ChannelPanel buildUpperPanel(){
        ChannelPanel channelPanel = new ChannelPanel();
        return channelPanel;
    }
    

    /**
     * Builds the upper panel by creating a JPanel and filling it with
     * a JTextField for inputting a classname and JButton
     *
     * @return The created ChannelPanel object
     */
    private ProgramPanel buildCenterPanel() {
        ProgramPanel programPanel = new ProgramPanel();
        return programPanel;
    }


    /**
     * Gets the item currently selected in the channel selector dropdown
     * menu
     * @return
     */
    public Object getSelectedValue(){
        return channelPanel.getChannelSelector().getSelectedItem();
    }


    /**
     * Inserts an item into the Jcombobox (dropdown menu) channel selector
     * @param channelName The name of the channel
     * @param index The index to add the item at
     */
    public void insertComboBoxItem(String channelName, int index){
        channelPanel.insertItem(channelName, index);
    }


    /**
     * Adds an episode to the program table
     * @param episode The episode to add
     */
    public void addTableItem(Episode episode){
        programpPanel.addTableItem(episode);
    }


    /**
     * Clears the table
     */
    public void clearTable(){
        programpPanel.clearTable();
    }


    /**
     * Clears the dropdown menu
     */
    public void clearDropDown(){
        channelPanel.clearDropDown();
    }


    /**
     * Getter for the program panel object
     * @return The programpanel object
     */
    public ProgramPanel getProgrampPanel() {
        return programpPanel;
    }


    /**
     * getter for the channel panel object
     * @return The channelpanel object
     */
    public ChannelPanel getChannelPanel() {
        return channelPanel;
    }

    
    /**
     * getter for the update button
     * @return The JmenuItem object that is the update button
     */
    public JMenuItem getUpdateButton() {
        return updateItem;
    }

    /**
     * Shows a popup with a containing message
     */
    public void showPopUp(String message){
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message);
        });
    }
}
