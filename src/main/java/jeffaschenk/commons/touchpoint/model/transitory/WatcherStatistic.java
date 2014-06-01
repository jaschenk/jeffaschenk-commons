package jeffaschenk.commons.touchpoint.model.transitory;


import jeffaschenk.commons.types.WatcherStatisticType;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Simple Transitory Task Object, which represent the
 * statistics for the various File Directories to Watch.
 *
 * @author jeffaschenk@gmail.com
 */
public class WatcherStatistic implements Serializable {

    private WatcherStatisticType watcherStatisticType;

    private Path watcherPath;

    private long totalFileCreations = 0;

    private long totalFileDeletions = 0;

    private long cycleFileCreations = 0;

    private long cycleFileDeletions = 0;

    private boolean cycleFilenamePrefixTriggerPresent = false;

    private boolean active = false;

    private long timeLastTrigger = 0;

    /**
     * Default Constructor
     */
    public WatcherStatistic() {
    }

    /**
     * Constructor Declaring Path.
     *
     * @param watcherStatisticType
     * @param watcherPath
     */
    public WatcherStatistic(WatcherStatisticType watcherStatisticType, Path watcherPath) {
        this.watcherStatisticType = watcherStatisticType;
        this.watcherPath = watcherPath;
        this.active = true;
    }

    public WatcherStatisticType getWatcherStatisticType() {
        return watcherStatisticType;
    }

    public void setWatcherStatisticType(WatcherStatisticType watcherStatisticType) {
        this.watcherStatisticType = watcherStatisticType;
    }

    public Path getWatcherPath() {
        return watcherPath;
    }

    public void setWatcherPath(Path watcherPath) {
        this.watcherPath = watcherPath;
    }

    public long getTotalFileCreations() {
        return totalFileCreations;
    }

    public void setTotalFileCreations(long totalFileCreations) {
        this.totalFileCreations = totalFileCreations;
    }

    public void incrementTotalFileCreations() {
        this.totalFileCreations++;
    }

    public long getTotalFileDeletions() {
        return totalFileDeletions;
    }

    public void setTotalFileDeletions(long fileDeletions) {
        this.totalFileDeletions = fileDeletions;
    }

    public void incrementTotalFileDeletions() {
        this.totalFileDeletions++;
    }

    public long getCycleFileCreations() {
        return cycleFileCreations;
    }

    public void setCycleFileCreations(long cycleFileCreations) {
        this.cycleFileCreations = cycleFileCreations;
    }

    public void incrementCycleFileCreations() {
        this.cycleFileCreations++;
    }

    public long getCycleFileDeletions() {
        return cycleFileDeletions;
    }

    public void setCycleFileDeletions(long cycleFileDeletions) {
        this.cycleFileDeletions = cycleFileDeletions;
    }

    public void incrementCycleFileDeletions() {
        this.cycleFileDeletions++;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCycleFilenamePrefixTriggerPresent() {
        return cycleFilenamePrefixTriggerPresent;
    }

    public void setCycleFilenamePrefixTriggerPresent(boolean cycleFilenamePrefixTriggerPresent) {
        this.cycleFilenamePrefixTriggerPresent = cycleFilenamePrefixTriggerPresent;
    }

    public long getTimeLastTrigger() {
        return timeLastTrigger;
    }

    public void setTimeLastTrigger(long timeLastTrigger) {
        this.timeLastTrigger = timeLastTrigger;
    }

    @Override
    public String toString() {
        return "WatcherStatistic{" +
                "watcherStatisticType=" + watcherStatisticType +
                ", watcherPath=" + watcherPath +
                ", totalFileCreations=" + totalFileCreations +
                ", totalFileDeletions=" + totalFileDeletions +
                ", cycleFileCreations=" + cycleFileCreations +
                ", cycleFileDeletions=" + cycleFileDeletions +
                ", cycleFilenamePrefixTriggerPresent=" + cycleFilenamePrefixTriggerPresent +
                ", active=" + active +
                ", timeLastTrigger=" + timeLastTrigger +
                '}';
    }

}
