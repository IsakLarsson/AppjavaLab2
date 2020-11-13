package View;

import Model.ComboListener;
import Model.Episode;
import Model.TableListener;
import Model.UpdateListener;

import javax.swing.*;
import java.awt.*;

public class GUI {
    private JFrame frame;
    private int size = 400;
    private JMenuItem updateItem;
    private JMenuItem aboutItem;

    ChannelPanel channelPanel;
    ProgramPanel programpPanel;


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
        //this.menuListener = menuListener;
        //this.buttonListener = buttonListener;
    }

    public void show() {
        frame.setVisible(true);
    }

    /**
     * Build the menu bar
     * @return The menu bar
     */
    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        updateItem = new JMenuItem("Uppdatera");

        fileMenu.add(updateItem);

        JMenu helpMenu = new JMenu("Help");
        aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    public void setupListeners(ComboListener comboListener,
                               TableListener tableListener, UpdateListener updateListener){
        channelPanel.getChannelSelector().addActionListener(comboListener);
        programpPanel.getTable().addMouseListener(tableListener);
        updateItem.addActionListener(updateListener);
    }

    public Object getSelectedValue(){
        return channelPanel.getChannelSelector().getSelectedItem();
    }

    private ChannelPanel buildUpperPanel(){
        /*JPanel channelPanel = new JPanel();
        JComboBox channelSelector = new JComboBox();
        channelSelector = new JComboBox();
        channelPanel.setLayout(new BorderLayout());
        channelPanel.add(channelSelector, BorderLayout.CENTER);*/
        ChannelPanel channelPanel = new ChannelPanel();
        return channelPanel;
    }

    /**
     * Builds the upper panel by creating a JPanel and filling it with
     * a JTextField for inputting a classname and JButton
     *
     * @return The created panel
     */
    private ProgramPanel buildCenterPanel() {
        /*TableModel tableModel = new DefaultTableModel();
        JPanel programPanel = new JPanel();
        String[] labels = {"Program", "Start-tid", "Slut-tid"};
        JTable table = new JTable(tableModel);
        programPanel.setLayout(new BorderLayout());
        table = new JTable(data, labels);
        JScrollPane scrollPane = new JScrollPane(table);
        programPanel.add(scrollPane);*/
        ProgramPanel programPanel = new ProgramPanel();
        return programPanel;
    }


    public void addTableItem(Episode episode){
        programpPanel.addTableItem(episode);
    }

    public void insertComboBoxItem(String channelName, int index){
        channelPanel.insertItem(channelName, index);
    }

    public void clearTable(){
        programpPanel.clearTable();
    }

    public void clearDropDown(){
        channelPanel.clearDropDown();
    }

    public ProgramPanel getProgrampPanel() {
        return programpPanel;
    }

    public ChannelPanel getChannelPanel() {
        return channelPanel;
    }

    public JMenuItem getUpdateButton() {
        return updateItem;
    }

}
