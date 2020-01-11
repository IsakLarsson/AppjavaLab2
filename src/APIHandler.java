import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class APIHandler {
    //TODO lägg in channels i en hashtabell eventuellt en egen klass

    public APIHandler(){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        String url = "http://api.sr.se/api/v2/channels/";

        Document doc = null;

        try {
            doc = getRootElement(db, url); // gets root element "sr"
            Element docElement = doc.getDocumentElement();
            NodeList elementChildren = docElement.getElementsByTagName(
                    "pagination");
            Node pagination = elementChildren.item(0);


            //get page info
            NodeList pagesnrList = doc.getElementsByTagName("totalpages");
            Node pagesnrreal = pagesnrList.item(0);
            Element pagenrStringelement = (Element) pagesnrreal;
            System.out.println("Nr of pages: "+ pagenrStringelement.getTextContent());

            NodeList nextPage = doc.getElementsByTagName("nextpage");
            Node nextpageNode = nextPage.item(0);
            Element nextPageElement = (Element)nextpageNode;
            System.out.println("Next Page " + nextPageElement.getTextContent());

            NodeList channels = doc.getElementsByTagName("channel") ;
            System.out.println("number of channels " + channels.getLength());

            for (int i = 0; i < channels.getLength(); i++){
                String idAndName = getChannelIDandName(channels, i);
                System.out.println(idAndName);
            }

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getChannelIDandName(NodeList channels, int index) {
        Node node = channels.item(index);
        Element channel = (Element) node;
        String nameAndID =
                (channel.getAttribute("id") + " " + channel.getAttribute(
                "name"));
        return nameAndID;
    }

    private Document getRootElement(DocumentBuilder db, String url) throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        System.out.println("Root: " + doc.getDocumentElement().getNodeName());
        return doc;
    }
}
