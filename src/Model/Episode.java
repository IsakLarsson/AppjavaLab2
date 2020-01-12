package Model;


public class Episode {
    private String startTimeUTC;
    private String endTimeUTC;
    private String title;
    private String description;

    public Episode(String title, String description, String start, String end){
        this.title = title;
        this.description = description;
        startTimeUTC = start;
        endTimeUTC = end;
    }

}
