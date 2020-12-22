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

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class for handling the channels in the background. Sort of the core in
 * the program
 */
public class ChannelHandler {
    private HashMap<String, Channel> channelMap;
    private TableauHandler tableauHandler;
    private MenuSetup setupHandler;
    private TableInterface tableEditor;
    JMenuItem updateButton;
    ScheduledExecutorService ses = 
        Executors.newSingleThreadScheduledExecutor(); //for running every hour

    /**
     * Constructor for the class. Sets the fields
     * @param channelMap The map containing the channels
     * @param setupHandler 
     * @param updateButton The update button in the gui
     * @param tableEditor The interface for editing the table
     */
    public ChannelHandler(HashMap channelMap, MenuSetup setupHandler, JMenuItem updateButton, TableInterface tableEditor){
        this.channelMap = channelMap;
        tableauHandler = new TableauHandler(channelMap);
        this.setupHandler = setupHandler;
        this.updateButton = updateButton;
        this.tableEditor = tableEditor;
    }

    /**
     * The main method for loading the individual channels from the api
     * Gets the channels and their attributes such as name and description
     * and adds them to the channelmap
     */
    public synchronized void loadChannels(){
        /**
         * Set update button to disabled while updating to make it impossible
         * to have two threads accessing the channelmap simultaneously
         */
        updateButton.setEnabled(false);

        channelMap.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, 
                    "XML builder failed: " + e.getMessage());
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
            JOptionPane.showMessageDialog(null, 
                    "Something went wrong when reading the XML document");
        } catch(IOException e) {
            System.out.println("Episode not found at: "+ 
                    e.getMessage()+ " skipping..");
        }
        setupHandler.setupDropDown();
        updateButton.setEnabled(true);
        JOptionPane.showMessageDialog(null, "Channels updated succesfully");
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
        channelMap.put(newChannel.getName(), newChannel);
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
        //System.out.println("Root: " + doc.getDocumentElement().getNodeName());
        return doc;
    }

    /**
     * Runs the handler
     */
    public void load() {
        ses.scheduleAtFixedRate(new Runnable(){
            public synchronized void run(){
                loadChannels();
                tableEditor.updateTable("P1");
            }
        }, 0, 1, TimeUnit.HOURS);  //Eun every hours
        
    }
}
