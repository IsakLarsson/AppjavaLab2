package Model;


import java.time.LocalDateTime;

public class Episode {
    private String startTimeUTC;
    private String endTimeUTC;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;
    private String imageURL;

    public Episode(String title, String description, String start, String end
            , String imageURL){
        this.title = title;
        this.description = description;
        startTimeUTC = start;
        endTimeUTC = end;
        this.imageURL = imageURL;
    }

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
