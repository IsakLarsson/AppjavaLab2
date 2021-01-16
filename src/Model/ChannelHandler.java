/**
 * Class for handling all the channels using a map consisting of channel
 * objects and strings as key-value pairs
 */

package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Interfaces.*;
import View.GUI;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Class for handling the channels in the background. Sort of the core in
 * the program
 */
public class ChannelHandler extends AbstractAction{
    private javax.swing.Timer fTimer;
    private ChannelMap channelMap;
    private TableauHandler tableauHandler;
    private MenuSetup setupHandler;
    private TableInterface tableEditor;
    JMenuItem updateButton;

    /**
     * Constructor for the class. Sets the fields
     * @param channelMap The map containing the channels
     * @param setupHandler 
     * @param updateButton The update button in the gui
     * @param tableEditor The interface for editing the table
     */
    public ChannelHandler(ChannelMap channelMap, MenuSetup setupHandler,
                          JMenuItem updateButton, TableInterface tableEditor){
        this.channelMap = channelMap;
        tableauHandler = new TableauHandler(channelMap.channels);
        this.setupHandler = setupHandler;
        this.updateButton = updateButton;
        this.tableEditor = tableEditor;

        //for running every hour
        fTimer = new javax.swing.Timer(3600000, this);
        fTimer.setInitialDelay(0);
    }

    /**
     * The main method for loading the individual channels from the api
     * Gets the channels and their attributes such as name and description
     * and adds them to the channelmap
     */
    public synchronized void loadChannels(){
        channelMap.loading = true;
        channelMap.channels.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            showMessage("XML builder failed: " + e.getMessage());
        }
        String url = "http://api.sr.se/api/v2/channels/";
        Document doc = null;

        try {
            doc = getRootElement(db, url); // gets root element "sr"

            //get page info
            nrOfPages = getNrOfPages(doc);

            for(int i = 0; i < nrOfPages; i++){
                String nextPageURL = null;

                NodeList channels = doc.getElementsByTagName("channel");
                NodeList taglineList = doc.getElementsByTagName("tagline");

                for (int j = 0; j < channels.getLength(); j++){
                    int channelID = getChannelID(channels, j);
                    String channelName = getChannelName(channels, j);
                    String taglineString = null;
                    String imageURL = getImageURL(channels, j);
                    taglineString = getTagline(taglineList, j);
                    addNewChannel(channelID, channelName,
                            taglineString, imageURL);

                }
                nextPageURL = getNextPage(doc, nextPageURL);
                if(nextPageURL!=null){
                    doc = getRootElement(db, nextPageURL);
                }

            }

        } catch (SAXException e){
            showMessage("Something went wrong when reading the XML" +
            " document");
        } catch(IOException e) {
            System.out.println("Episode not found at: "+ 
                    e.getMessage()+ " skipping..");
        }
        
        showMessage("Channels updated successfully");
        channelMap.loading = false;
    }

    /**
     * Helper method for displaying JOptionPanes on the EDT
     * @param message The message to display
     */
    private synchronized void showMessage(String message){
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(null, message);
        });
    }


    /**
     * Get the number of pages in the given document
     * @param doc The given document
     * @return The number of pages as an int
     */
    private int getNrOfPages(Document doc) {
        int nrOfPages;
        NodeList pagesnrList = doc.getElementsByTagName("totalpages");
        Node pagesNrNode = pagesnrList.item(0);
        Element pagenrStringelement = (Element) pagesNrNode;
        nrOfPages = Integer.parseInt(pagenrStringelement.getTextContent());
        return nrOfPages;
    }

    /**
     * Get the url of the next page in the document
     * @param doc The given document
     * @return The URL of the next page
     */
    private String getNextPage(Document doc, String nextPageURL) {
        NodeList nextPage = doc.getElementsByTagName("nextpage");
        Node nextpageNode = nextPage.item(0);
        Element nextPageElement = (Element)nextpageNode;
        if (nextPageElement!=null){
            nextPageURL = nextPageElement.getTextContent();
        }
        return nextPageURL;
    }

    /**
     * Adds a new channel to the channel map and loads its episodes through
     * the tableau handler 
     * @param channelID The channel ID
     * @param channelName The channel name
     * @param taglineString The channel tagline
     * @param imageURL The image URL
     */
    private void addNewChannel(int channelID, String channelName,
                               String taglineString, String imageURL) {
        Channel newChannel = new Channel(channelID, channelName,
                taglineString, imageURL);
        channelMap.channels.put(newChannel.getName(), newChannel);
        //System.out.println("Added channel: " + channelName + " " +
        //        taglineString);
        tableauHandler.loadEpisodes(newChannel);
    }

    /**
     * Gets the channel tagline
     * @param tagline The list of taglines
     * @param index the channels index
     * @return The channels tagline as a string
     */
    private String getTagline(NodeList tagline, int index) {    
        String taglineString;
        Node taglineNode = tagline.item(index);
        Element taglineElement = (Element)taglineNode;
        taglineString = taglineElement.getTextContent();
        return taglineString;
    }

    /**
     * Gets the image URL of a channel
     * @param channels The list of channels
     * @param index The channels index
     * @return The image url as a string
     */
    private String getImageURL(NodeList channels, int index) {
        String urlString = null;
        Node node = channels.item(index);
        Element channel = (Element) node;
        NodeList sublist = channel.getElementsByTagName("image");
        Node url = sublist.item(0);
        Element urlElement = (Element)url;
        if (urlElement != null){
            urlString = urlElement.getTextContent();
        }
        return urlString;
    }

    /**
     * Gets the channel id of a given channel
     * @param channels The list of channels
     * @param index The index of the channel
     * @return The ID as an int
     */
    private int getChannelID(NodeList channels, int index){
        Node node = channels.item(index);
        Element channel = (Element) node;
        int ID = Integer.parseInt(channel.getAttribute("id"));
        return ID;
    }

    /**
     * Gets the channel name of a given channel
     * @param channels The list of channels
     * @param index The index of the channel
     * @return The name of the channel
     */
    private String getChannelName(NodeList channels, int index) {
        Node node = channels.item(index);
        Element channel = (Element) node;
        String name = channel.getAttribute("name");
        return name;
    }

    /**
     * Gets the root element of the document
     * @param db The document builder object
     * @param url The url of the root element
     * @return The document as a Document object
     * @throws SAXException
     * @throws IOException
     */
    private Document getRootElement(DocumentBuilder db, String url)
            throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        //System.out.println("Root: " +
        // doc.getDocumentElement().getNodeName());
        return doc;
    }

    /**
     * Starts the timer which updates the channels once every hour
     */
    public void startHandler() {
        fTimer.start();
    }

    /**
     * Updates the channels on a SwingWorker background thread and updates
     * the GUI when done
     * @param evt
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        /**
         * Set update button to disabled while updating to make it impossible
         * to have two threads accessing the channelmap simultaneously
         */
        updateButton.setEnabled(false);
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                if (!channelMap.loading){
                    loadChannels();
                } else {
                    System.out.println("Update in progress, please try later");
                }
                return null;
            }

            @Override
            protected void done() {
                String defaultChannel = "P1";
                tableEditor.updateTable(defaultChannel);
                setupHandler.setupDropDown();
                updateButton.setEnabled(true);
                super.done();
            }
        };
        worker.execute();
    }
    
}
