package View;

import Model.Episode;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProgramPanel extends JPanel{
    String[] labels = {"Program", "Start-tid", "Slut-tid"};
    Object[][] data = {{"program", "tid", "tid"}};
    private JTable table;
    private DefaultTableModel tableModel = new DefaultTableModel();
    private int currentPlayingRow = 4;

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

    public void addTableItem(Episode episode){
        Object[] item = {episode.getTitle(), episode.getStartString(),
                episode.getEndString()};
        tableModel.addRow(item);
    }

    public void clearTable(){
        tableModel.setRowCount(0);
    }

    public JTable getTable() {
        return table;
    }

    public Object[][] getData() {
        return data;
    }

}
