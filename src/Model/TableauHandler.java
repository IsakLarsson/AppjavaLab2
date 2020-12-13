package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class for handling the HashMap that contains the channels
 */
public class TableauHandler {
    HashMap channelMap;

    /**
     * Constructor for the class
     * @param channelMap The hashmap containing the channels
     */
    public TableauHandler(HashMap channelMap){
        this.channelMap = channelMap;
    }

    /**
     * Loads the episodes for a given channel from the SR API and adds the
     * episodes to the channel objects episode list.
     * @param channel The given channel
     */
    public synchronized void loadEpisodes(Channel channel){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        int nrOfPages;
        ArrayList<String> urlList = new ArrayList<String>();
        
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            JOptionPane.showMessageDialog(null, "Parser Error:" +
                 e.getMessage());
        }
        
        getTableauDays(channel, urlList);

        Document doc = null;
        for (String url : urlList) {
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

                        //om tiden är mer än 12 timmar bak eller fram, skippa
                        if(checkTimeWindow(getEpisodeStartTime(doc, j))){
                            channel.addEpisode(getEpisode(doc, 
                                episodes, descriptionList, j));
                        }else{
                            continue;
                        }

                    }
    
                    nextPageURL = getNextPage(doc);
                    if(nextPageURL!=null){
                        doc = getRootElement(db, nextPageURL);
                    }
                }
    
            } catch (SAXException e) {
                JOptionPane.showMessageDialog(null, 
                    "Something went wrong when reading the XML document");
            } catch (IOException e) {
                System.out.println("Channel not found at: "+ 
                    e.getMessage()+ " skipping..");
            }
        }
    }


    
    /**
     * Gets the days that will be read from the API since we want to show
     * 12h before and 12h after the current program at all times which will
     * result in two days retreived
     * @param channel The channel to get from
     * @param urlList The list of urls representing the days
     */
    private void getTableauDays(Channel channel, ArrayList<String> urlList) {
        String urlDay1 =
                "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channel.getID() +
                "&date=" + getCurrentDate(0);

        //If before 12:00, get the day before as well, otherwise get the day after
        if(getCurrentTime().contains("AM")){
            String urlDay2 = 
                "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channel.getID() +
                "&date=" + getCurrentDate(-1);
            urlList.add(urlDay2);
            urlList.add(urlDay1);
        }else{
            String urlDay2 = 
                "http://api.sr.se/api/v2/scheduledepisodes?channelid=" + channel.getID() +
                "&date=" + getCurrentDate(1);
            urlList.add(urlDay1);
            urlList.add(urlDay2);
        }
    }

    /**
     * Gets a single episode and all its content from the lists
     * retreived from the API
     * @param doc The root element
     * @param episodes The list of episodes 
     * @param descriptionList The list of episode descriptions
     * @param j index
     */
    private Episode getEpisode(Document doc, NodeList episodes, 
            NodeList descriptionList, int j) {

        Episode episode;
        String title = getEpisodeTitle(episodes, j);
        String description = getDescription(descriptionList,j);
        String startTime = getEpisodeStartTime(doc, j);
        String endTIme = getEpisodeEndTime(doc, j);
        String imageURL = getImageURL(episodes, j);
        episode = new Episode(title, description, startTime,
                endTIme, imageURL);
        episode.setStartTime(parseStartTime(episode)); 
        episode.setEndTime(parseEndTime(episode));
        return episode;
    }

    /**
     * Gets the root element of the XML document
     * @param db Documentbuilder object
     * @param url URL of the document
     * @return The retreived document
     * @throws SAXException
     * @throws IOException
     */
    private Document getRootElement(DocumentBuilder db, String url) throws SAXException, IOException {
        Document doc;
        doc = db.parse(new URL(url).openStream());
        return doc;
    }

    /**
     * Gets the title of an episode
     * @param episodes The list of episodes
     * @param index index of episode in list
     * @return
     */
    private String getEpisodeTitle(NodeList episodes, int index) {
        Node node = episodes.item(index);
        Element episode = (Element) node;
        NodeList sublist = episode.getElementsByTagName("title");
        Node title = sublist.item(0);
        Element titleElement = (Element)title;
        String titleString = titleElement.getTextContent();
        return titleString;
    }
    
    /**
     * Gets the number of pages in the document
     * @param doc The document object
     * @return The number of pages
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
     * Gets the next page of the document
     * @param doc The given document
     * @return The URL of the next page
     */
    private String getNextPage(Document doc) {
        NodeList nextPage = doc.getElementsByTagName("nextpage");
        Node nextpageNode = nextPage.item(0);
        Element nextPageElement = (Element)nextpageNode;
        String nextPageURL= null;
        if (nextPageElement!=null){
            nextPageURL = nextPageElement.getTextContent();
        }
        return nextPageURL;
    }

    /**
     * Gets the description of the episode at a given index
     * @param descriptionList The list of descriptions
     * @param index The index of the episode
     * @return The description as a string
     */
    private String getDescription(NodeList descriptionList, int index) {
        String descriptionString = "Description not available";
        Node descriptionNode = descriptionList.item(index);
        Element taglineElement = (Element)descriptionNode;
        if (taglineElement != null){
            descriptionString = taglineElement.getTextContent();
        }
        return descriptionString;
    }

    /**
     * Gets the episode start time of the episode at a given index
     * @param doc The document
     * @param index The index of the episode
     * @return
     */
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

    /**
     * Gets the episode end time of the episode at a given index
     * @param doc The document
     * @param index The index of the episode
     * @return
     */
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

    /**
     * Gets the image URL of an episode at a given index
     * @param episodes The list of episodes
     * @param index The index of the episode
     * @return The image URL as a string
     */
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

    /**
     * Getting the current date or another offset date
     * @param offsetDays the number of offset days, i.e 1 for tomorrow, -1 for 
     *                   yesterday, 0 for today
     * @return The date formatted as ("yyyy/mm/dd")
     */
    private String getCurrentDate(int offsetDays){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now.plusDays(offsetDays));
    }

    /**
     * Gets the current time of the system
     * @return The current time as a string formatted as (HH:MM AM/PM)
     */
    private String getCurrentTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    /**
     * Checks if the starttime of an episode is within a 12hr window before
     * and after the current system time.
     * @param startTime Start time of the episode
     * @return True if within window, false otherwise
     */
    private boolean checkTimeWindow(String startTime){
        ZonedDateTime start = ZonedDateTime.parse(startTime);
        ZonedDateTime windowStart = ZonedDateTime.now().plusHours(-12);
        ZonedDateTime windowEnd = ZonedDateTime.now().plusHours(12);
        
        if(start.isAfter(windowStart) && start.isBefore(windowEnd)){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Parses the starttime of an episode into a LocalDateTime object
     * @param episode The given episode
     * @return The starttime as a LocalDateTime
     */
    public LocalDateTime parseStartTime(Episode episode){
        String start = episode.getStartTimeUTC();
        ZonedDateTime zStart = ZonedDateTime.parse(start);
        return LocalDateTime.ofInstant(zStart.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Parses the end time of an episode into a LocalDateTime object
     * @param episode The given episode
     * @return The starttime as a LocalDateTime
     */
    public LocalDateTime parseEndTime(Episode episode){
        String end = episode.getEndTimeUTC();
        ZonedDateTime zEnd = ZonedDateTime.parse(end);
        return LocalDateTime.ofInstant(zEnd.toInstant(),
                ZoneId.systemDefault());
    }
}
