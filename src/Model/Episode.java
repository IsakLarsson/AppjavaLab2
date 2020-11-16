package Model;
import java.time.LocalDateTime;

/**
 * Episode class that contains its attributes like start time
 * end time, title, description and image URL
 */
public class Episode {
    private String startTimeUTC;
    private String endTimeUTC;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private String imageURL;

    /**
     * Constructor for the episode
     * @param title The episode title
     * @param description The episode description 
     * @param start The starttime in UTC
     * @param end The endtime in UTC
     * @param imageURL The image URL
     */
    public Episode(String title, String description, String start, String end
            , String imageURL){
        this.title = title;
        this.description = description;
        startTimeUTC = start;
        endTimeUTC = end;
        this.imageURL = imageURL;
    }

    /**
     * Get the time in hh:mm format
     * @param time The time in LocalDateTime format
     * @return episode time in hh:mm format
     */
    private String getTimes(LocalDateTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String hourString = String.valueOf(hour);
        String minuteString = String.valueOf(minute);

        if(hour < 10){
            hourString = "0" + hour;
        }
        if(minute < 10){
            minuteString = "0" + minute;
        }
        return hourString + ":" + minuteString;
    }

    /**
     * Getter and setter helper functions for getting and setting fields
     * of the class
     */

    public String getStartTimeUTC() {
        return startTimeUTC;
    }

    public String getEndTimeUTC() {
        return endTimeUTC;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getStartString() {
        return getTimes(startTime);
    }

    public String getEndString() {
        return getTimes(endTime);
    }

    

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }
}
