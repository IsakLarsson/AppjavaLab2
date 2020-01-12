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

public class TableauHandler {
    HashMap channelMap;

    public TableauHandler(HashMap channelMap){
        this.channelMap = channelMap;
    }

    public void loadEpisodes(String channelID){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        String url = "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channelID;
        Document doc = null;

        try {
            doc = getRootElement(db, url); // gets root element "sr"

            nrOfPages = getNrOfPages(doc);

            for(int i = 0; i < nrOfPages; i++){
                String nextPageURL = null;

                NodeList episodes = doc.getElementsByTagName(
                        "scheduledepisode");
                NodeList descriptionList = doc.getElementsByTagName(
                        "description");


                for(int j = 0; j < episodes.getLength(); j++){

                }
                //get episodes from api and add to channels episodelist


                nextPageURL = getNextPage(doc, nextPageURL);
                if(nextPageURL!=null){
                    doc = getRootElement(db, nextPageURL);
                }

            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private Document getRootElement(DocumentBuilder db, String url) throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        System.out.println("Root: " + doc.getDocumentElement().getNodeName());
        return doc;
    }

    private int getNrOfPages(Document doc) {
        int nrOfPages;
        NodeList pagesnrList = doc.getElementsByTagName("totalpages");
        Node pagesNrNode = pagesnrList.item(0);
        Element pagenrStringelement = (Element) pagesNrNode;
        System.out.println("Nr of pages: "+ pagenrStringelement.getTextContent());
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

    private String getDescription(NodeList descriptionList, int index) {
        String descriptionString;
        Node descriptionNode = descriptionList.item(index);
        Element taglineElement = (Element)descriptionNode;
        descriptionString = taglineElement.getTextContent();
        return descriptionString;
    }


}
