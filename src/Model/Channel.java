package Model;

import java.util.ArrayList;

public class Channel {
    private int ID;
    private String name;
    private String tagline;
    private String channelType;
    private String imageURL;
    private ArrayList<Episode> tableauList;

    public Channel(int ID, String name, String tagline, String imageURL){
        this.ID = ID;
        this.name = name;
        this.tagline = tagline;
        tableauList = new ArrayList<>();
    }

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
}
