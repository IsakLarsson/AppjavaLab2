/**
 * Class for abstracting a radiochannel, contains its attributes like
 * name, tagline etc
 */

package Model;

import java.util.ArrayList;

public class Channel {
    private int ID;
    private String name;
    private String tagline;
    private String channelType;
    private String imageURL;
    private ArrayList<Episode> tableauList;

    /**
     * Constructor method
     * @param ID the channel ID
     * @param name The channel name
     * @param tagline  The channel tagline
     * @param imageURL The URL for the channel thumbnail image
     */
    public Channel(int ID, String name, String tagline, String imageURL){
        this.ID = ID;
        this.name = name;
        this.tagline = tagline;
        tableauList = new ArrayList<>();
        this.imageURL = imageURL;
    }


    /**
     * Helper methods for getting and setting attributes
     */

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void addEpisode(Episode episode){
        tableauList.add(episode);
    }

    public ArrayList<Episode> getTableauList() {
        return tableauList;
    }
}
