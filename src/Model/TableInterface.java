package Model;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Interface for defining the Table interface methods
 */
public interface TableInterface {

    /**
     * Updates the table contents of a given channel
     * @param channel The channel name to update
     */
    public void updateTable(String channel);

    /**
     * Clears the table contents
     */
    public void clearTable();

    /**
     * Gets the current playing programs rownumber in the table
     * @param channel The channel name to check
     */
    public void getCurrentPlaying(String channel);

    /**
     * Gets the starttime of a given episode
     * @param episode The episode to get starttime from
     * @return The start time as a LocadDateTime object
     */
    public LocalDateTime getStartTime(Episode episode);

    /**
     * Gets the endtime of a given episode
     * @param episode The episode to get endtime from
     * @return The end time as a LocadDateTime object
     */
    public LocalDateTime getEndTime(Episode episode);

}
