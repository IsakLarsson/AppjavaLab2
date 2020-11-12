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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class TableauHandler {
    HashMap channelMap;

    public TableauHandler(HashMap channelMap){
        this.channelMap = channelMap;
    }

    public void loadEpisodes(Channel channel){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;

        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        String url =
                "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channel.getID() +
                        "&date=" + getCurrentDate();

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
                    //create episodes from api and add to channels episodelist
                    Episode episode;
                    String title = getEpisodeTitle(episodes, j);
                    String description = getDescription(descriptionList,j);
                    String startTime = getEpisodeStartTime(doc, j);
                    String endTIme = getEpisodeEndTime(doc, j);
                    String imageURL = getImageURL(episodes, j);
                    episode = new Episode(title, description, startTime,
                            endTIme, imageURL);
                    episode.setStartTime(getStartTime(episode));
                    episode.setEndTime(getEndTime(episode));
                    channel.addEpisode(episode);
                }

                nextPageURL = getNextPage(doc, nextPageURL);
                if(nextPageURL!=null){
                    doc = getRootElement(db, nextPageURL);
                }

            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Channel not found");
        }

    }


    private Document getRootElement(DocumentBuilder db, String url) throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        //System.out.println("Root: " + doc.getDocumentElement().getNodeName());
        return doc;
    }

    private String getEpisodeTitle(NodeList episodes, int index) {
        Node node = episodes.item(index);
        Element episode = (Element) node;
        NodeList sublist = episode.getElementsByTagName("title");
        Node title = sublist.item(0);
        Element titleElement = (Element)title;
        String titleString = titleElement.getTextContent();
        return titleString;
    }

    private int getNrOfPages(Document doc) {
        int nrOfPages;
        NodeList pagesnrList = doc.getElementsByTagName("totalpages");
        Node pagesNrNode = pagesnrList.item(0);
        Element pagenrStringelement = (Element) pagesNrNode;
        //System.out.println("Nr of pages: "+ pagenrStringelement
        // .getTextContent());
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
        String descriptionString = "Description not available";
        Node descriptionNode = descriptionList.item(index);
        Element taglineElement = (Element)descriptionNode;
        if (taglineElement != null){
            descriptionString = taglineElement.getTextContent();
        }
        return descriptionString;
    }

    private String getEpisodeStartTime(Document doc, int index) {
        String timeString = null;
        NodeList startTimeList = doc.getElementsByTagName("starttimeutc");
        Node startTimeNode = startTimeList.item(index);
        Element startTimeElement = (Element) startTimeNode;
        if(startTimeElement != null){
            timeString = startTimeElement.getTextContent();
        }
        return timeString;
    }

    private String getEpisodeEndTime(Document doc, int index) {
        String timeString = null;
        NodeList endTimeList = doc.getElementsByTagName("endtimeutc");
        Node endTimeNode = endTimeList.item(index);
        Element endTimeElement = (Element) endTimeNode;
        if(endTimeElement != null){
            timeString = endTimeElement.getTextContent();
        }
        return timeString;
    }

    private String getImageURL(NodeList episodes, int index) {
        String urlString = null;
        Node node = episodes.item(index);
        Element episode = (Element) node;
        NodeList sublist = episode.getElementsByTagName("imageurl");
        Node url = sublist.item(0);
        Element urlElement = (Element)url;
        if (urlElement != null){
            urlString = urlElement.getTextContent();
        }
        return urlString;
    }

    private String getCurrentDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
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
    //TODO fixa tidjäveln 12h innan och efter, ladda in gårdagens och dagens
    // och filtrera ut sedan
}
