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


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ChannelHandler extends Thread {
    private HashMap<String, Channel> channelMap;
    private TableauHandler tableauHandler;

    //TODO dont trust the debuggers lies


    public ChannelHandler(HashMap channelMap){
        this.channelMap = channelMap;
        tableauHandler = new TableauHandler(channelMap);
       // loadChannels();
    }

    public void loadChannels(){

        channelMap.clear();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
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

                //System.out.println("number of channels on this page: " +
                //      channels.getLength() +"\n");

                for (int j = 0; j < channels.getLength(); j++){
                    int channelID = getChannelID(channels, j);
                    String channelName = getChannelName(channels, j);
                    String taglineString = null;
                    String imageURL = getImageURL(channels, j);
                    taglineString = getTagline(taglineList, j);
                    addNewChannel(channelID, channelName,
                            taglineString, imageURL);

                }
                //This is a little bit weird but hey it works
                nextPageURL = getNextPage(doc, nextPageURL);
                if(nextPageURL!=null){
                    doc = getRootElement(db, nextPageURL);
                }

            }

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewChannel(int channelID, String channelName,
                               String taglineString, String imageURL) {
        Channel newChannel = new Channel(channelID, channelName,
                taglineString, imageURL);
        channelMap.put(newChannel.getName(), newChannel);
        System.out.println("Added channel: " + channelName + " " +
                taglineString);
        tableauHandler.loadEpisodes(newChannel);
    }

    private String getTagline(NodeList tagline, int index) {
        String taglineString;
        Node taglineNode = tagline.item(index);
        Element taglineElement = (Element)taglineNode;
        taglineString = taglineElement.getTextContent();
        return taglineString;
    }

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

    private int getNrOfPages(Document doc) {
        int nrOfPages;
        NodeList pagesnrList = doc.getElementsByTagName("totalpages");
        Node pagesNrNode = pagesnrList.item(0);
        Element pagenrStringelement = (Element) pagesNrNode;
        System.out.println("Nr of pages: "+
                pagenrStringelement.getTextContent());
        nrOfPages = Integer.parseInt(pagenrStringelement.getTextContent());
        return nrOfPages;
    }


    private String getNextPage(Document doc, String nextPageURL) {
        NodeList nextPage = doc.getElementsByTagName("nextpage");
        Node nextpageNode = nextPage.item(0);
        Element nextPageElement = (Element)nextpageNode;
        if (nextPageElement!=null){
            nextPageURL = nextPageElement.getTextContent();
        }
        return nextPageURL;
    }


    private int getChannelID(NodeList channels, int index){
        Node node = channels.item(index);
        Element channel = (Element) node;
        int ID = Integer.parseInt(channel.getAttribute("id"));
        return ID;
    }


    private String getChannelName(NodeList channels, int index) {
        Node node = channels.item(index);
        Element channel = (Element) node;
        String name = channel.getAttribute("name");
        return name;
    }


    private Document getRootElement(DocumentBuilder db, String url)
            throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        //System.out.println("Root: " + doc.getDocumentElement().getNodeName());
        return doc;
    }

    @Override
    public void run() {
        loadChannels();
    }
}
