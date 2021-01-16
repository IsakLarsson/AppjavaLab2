package Model;

import View.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for editing the table containing the episodes of a given
 * channel, implements the TableInterface
 */
public class TableEditor implements TableInterface {
    private ChannelMap channelMap;
    private GUI gui;
    private int currentPlayingRow = 0;

    /**
     * Constructor for the class
     * @param channelMap The map containing the channels
     * @param gui The GUI
     */
    public TableEditor (ChannelMap channelMap, GUI gui){
        this.channelMap = channelMap;
        this.gui = gui;
    }

    
    /**
     * Updates the table contents of a given channel
     * @param channel The channel name to update
     */
    @Override
    public synchronized void updateTable(String channel) {
        clearTable();
        getCurrentPlaying(channel);
        for (int i = 0; i < 3; i++){
            gui.getProgrampPanel().getTable().getColumnModel().
            getColumn(i).setCellRenderer(new PlayedCellRenderer());
        }
        Channel channelToUpdate = channelMap.channels.get(channel);
        ArrayList episodes = channelToUpdate.getTableauList();
        for (int i = 0; i < episodes.size(); i++){
            gui.addTableItem(channelMap.channels.get(channel).
                getTableauList().get(i));
        }
    }

    /**
     * Clears the table contents
     */
    @Override
    public void clearTable() {
        gui.clearTable();
    }

    /**
     * Gets the current playing programs rownumber in the table
     * @param channel The channel name to check
     */
    public void getCurrentPlaying(String channel){
        currentPlayingRow = -1;
        ArrayList episodeList = channelMap.channels.get(channel).getTableauList();

        for(int i = 0; i < episodeList.size(); i++){
            //get episode
            Episode episode = (Episode) episodeList.get(i);
            
            //check starttime
            if (episode.getStartTime().isBefore(LocalDateTime.now())) {
                // episode is earlier
                currentPlayingRow++;
            }
        }
    }

    /**
     * Gets the starttime of a given episode
     * @param episode The episode to get starttime from
     * @return The start time as a LocadDateTime object
     */
    public LocalDateTime getStartTime(Episode episode){
        String start = episode.getStartTimeUTC();
        ZonedDateTime zStart = ZonedDateTime.parse(start);
        return LocalDateTime.ofInstant(zStart.toInstant(), 
            ZoneId.systemDefault());
    }

    /**
     * Gets the endtime of a given episode
     * @param episode The episode to get endtime from
     * @return The end time as a LocadDateTime object
     */
    public LocalDateTime getEndTime(Episode episode){
        String end = episode.getEndTimeUTC();
        ZonedDateTime zEnd = ZonedDateTime.parse(end);
        return LocalDateTime.ofInstant(zEnd.toInstant(),
                ZoneId.systemDefault());
    }

    /**
     * Class to create the individual sells in the table, creates them
     * in different colors depending on if the program has passed or not
     */
    private class PlayedCellRenderer extends DefaultTableCellRenderer
    {   
        /**
        * Gets an individual cell for displaying in the table
        * @param table  the <code>JTable</code>
        * @param value  the value to assign to the cell at
        *                  [row, column]
        * @param isSelected true if cell is selected
        * @param hasFocus true if cell has focus
        * @param row  the row of the cell to render
        * @param column the column of the cell to render
        * @return the default table cell renderer
        */
        public Component getTableCellRendererComponent(JTable table,
                Object value, boolean isSelected, boolean hasFocus, 
                    int row, int column) {

            Component cell = 
                super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);

            if (row < currentPlayingRow) {
                cell.setForeground(Color.GRAY);
            } else if (row == currentPlayingRow) {
                cell.setForeground(Color.BLUE);
            } else {
                cell.setForeground(Color.BLACK);
            }
            return cell;
        }
    }
}
