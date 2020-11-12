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

public class TableEditor implements TableInterface {
    private HashMap<String, Channel> channelMap;
    private GUI gui;
    private int currentPlayingRow = 0;

    public TableEditor (HashMap<String, Channel> channelMap, GUI gui){
        this.channelMap = channelMap;
        this.gui = gui;
    }

    @Override
    public synchronized void updateTable(String channel) {
        clearTable();
        getCurrentPlaying(channel);
        for (int i = 0; i < 3; i++){
            gui.getProgrampPanel().getTable().getColumnModel().getColumn(i).setCellRenderer(new PlayedCellRenderer());
        }
        Channel channelToUpdate = channelMap.get(channel);
        ArrayList episodes = channelToUpdate.getTableauList();
        for (int i = 0; i < episodes.size(); i++){
            gui.addTableItem(channelMap.get(channel).getTableauList().get(i));
        }
    }

    @Override
    public void clearTable() {
        gui.clearTable();
    }

    public void getCurrentPlaying(String channel){
        currentPlayingRow = 0;
        ArrayList episodeList = channelMap.get(channel).getTableauList();

        for(int i = 0; i < episodeList.size(); i++){
            //hämta episode
            Episode episode = (Episode) episodeList.get(i);
            //kolla starttiden
            LocalDateTime start = episode.getStartTime();
            LocalDateTime now = LocalDateTime.now();

            if (episode.getStartTime().isBefore(LocalDateTime.now())) {
                // episode is earlier
                currentPlayingRow++;
                System.out.println(currentPlayingRow);
                //return;
            }
        }
    }

    public LocalDateTime getStartTime(Episode episode){
        String start = episode.getStartTimeUTC();
        ZonedDateTime zStart = ZonedDateTime.parse(start);
        return LocalDateTime.ofInstant(zStart.toInstant(), ZoneId.systemDefault());
    }

    public LocalDateTime getEndTime(Episode episode){
        String end = episode.getEndTimeUTC();
        ZonedDateTime zEnd = ZonedDateTime.parse(end);
        return LocalDateTime.ofInstant(zEnd.toInstant(),
                ZoneId.systemDefault());
    }


    private class PlayedCellRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
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
