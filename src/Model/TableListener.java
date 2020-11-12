package Model;

import View.GUI;
import View.ProgramPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;

public class TableListener extends MouseAdapter {
    GUI gui;
    private HashMap<String, Channel> channelMap;

    public TableListener(GUI gui, HashMap<String, Channel> channelMap){
        this.gui = gui;
        this.channelMap = channelMap;
    }

    public void mouseReleased(MouseEvent me) {
        Episode episode = getEpisode(me);
        String description = episode.getDescription();
        try {
            String urlString = episode.getImageURL();
            if (urlString != null){
                ImageIcon icon = loadImage(episode.getImageURL());
                JOptionPane.showMessageDialog(null, description,
                        "Beskrivning",
                        JOptionPane.INFORMATION_MESSAGE,icon);
            }else{
                urlString = "https://via.placeholder" +
                        ".com/200/000000/FFFFFF/?text=ImageNotAvailable";
                ImageIcon icon = loadImage(urlString);
                JOptionPane.showMessageDialog(null, description,
                        "Beskrivning",
                        JOptionPane.INFORMATION_MESSAGE,icon);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading image, try again");
        }
    }

    private Episode getEpisode(MouseEvent me) {
        ProgramPanel programPanel = gui.getProgrampPanel();
        int row = programPanel.getTable().rowAtPoint(me.getPoint());
        String channelName =
                gui.getChannelPanel().getChannelSelector().getSelectedItem().toString();
        Channel channel = channelMap.get(channelName);
        return channel.getTableauList().get(row);
    }

    private ImageIcon loadImage(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        BufferedImage image = ImageIO.read(is);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
