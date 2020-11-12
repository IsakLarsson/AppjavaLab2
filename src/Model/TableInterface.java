package Model;

import java.time.LocalDateTime;
import java.util.HashMap;

public interface TableInterface {
    public void updateTable(String channel);

    public void clearTable();

    public void getCurrentPlaying(String channel);

    public LocalDateTime getStartTime(Episode episode);

    public LocalDateTime getEndTime(Episode episode);

}
