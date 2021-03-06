package Model;

import View.GUI;
import View.ProgramPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * Actionlistener class for listening to mouse events from the table
 * and displaying program info
 */
public class TableListener extends MouseAdapter {
    GUI gui;
    private ChannelMap channelMap;


    /**
     * Constructor for the listener
     * @param gui The GUI
     * @param channelMap The Hashmap containing the channels
     */
    public TableListener(GUI gui, ChannelMap channelMap){
        this.gui = gui;
        this.channelMap = channelMap;
    }


    /**
     * Listens for mouse events from the table and displays extra program
     * information and the program picture if available.
     */
    public void mouseReleased(MouseEvent me) {
        Episode episode = getEpisode(me);
        String description = episode.getDescription();
        try {
            String urlString = episode.getImageURL();
            if (urlString != null){
                ImageIcon icon = loadImage(episode.getImageURL());
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, description,
                        "Beskrivning",
                        JOptionPane.INFORMATION_MESSAGE,icon);
                });
                
            }else{
                urlString = "https://via.placeholder" +
                        ".com/200/000000/FFFFFF/?text=ImageNotAvailable";
                ImageIcon icon = loadImage(urlString);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(null, description,
                    "Beskrivning",
                    JOptionPane.INFORMATION_MESSAGE,icon);
                });
                
            }
        } catch (IOException e) {
            gui.showPopUp( "Error loading image, try again");
        }
    }

    

    /**
     * Gets the clicked episode and retrieves the episode object from
     * channel map
     * @param me Mouse Event
     * @return The retreived episode
     */
    private Episode getEpisode(MouseEvent me) {
        ProgramPanel programPanel = gui.getProgrampPanel();
        int row = programPanel.getTable().rowAtPoint(me.getPoint());
        String channelName = "";
        
        try {
            channelName = gui.getChannelPanel().getChannelSelector().
                getSelectedItem().toString();
        } catch (NullPointerException e) {
            channelName = "P1";
        }
        
        Channel channel = channelMap.channels.get(channelName);
        return channel.getTableauList().get(row);
    }

    /**
     * Loads an image from a URL string
     * @param urlString The URL string to load from
     * @return The loaded image as an ImageIcon
     * @throws IOException IO Exception if image is not found
     */
    private ImageIcon loadImage(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream is = url.openStream();
        BufferedImage image = ImageIO.read(is);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }
}
