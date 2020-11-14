package View;
import Model.Episode;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Panel component for showing the program info about different radioprograms
 */
public class ProgramPanel extends JPanel{
    String[] labels = {"Program", "Start-tid", "Slut-tid"};
    Object[][] data = {{"program", "tid", "tid"}}; 
    private JTable table;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private int currentPlayingRow = 4;

    /**
     * Constructor for the panel, sets the layout type and sets up
     * the table.
     */
    public ProgramPanel(){
        setLayout(new BorderLayout());
        table = new JTable(tableModel);
        for(Object s : labels){
            tableModel.addColumn(s);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setEnabled(false);
        add(scrollPane);
        tableModel.addRow(new String[]{"Loading, please wait", "...", "..."});
    }

    /**
     * Adds a single table item to the table, takes an episode Object
     * as input
     * @param episode the episode object to add 
     */
    public void addTableItem(Episode episode){
        Object[] item = {episode.getTitle(), episode.getStartString(),
                episode.getEndString()};
        tableModel.addRow(item);
    }


    /**
     * Clears the table
     */
    public void clearTable(){
        tableModel.setRowCount(0);
    }

    /**
     * Function to get the table reference
     * @return returns the table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Function to get the object data from the table
     * @return the object data as a 2D Object array
     */
    public Object[][] getData() {
        return data;
    }

}
