package jeffaschenk.commons.touchpoint.model.transitory;

import jeffaschenk.commons.touchpoint.model.RootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Transitory Task Object, which represent the
 * Export Task being performed and Queued within a Thread Pool.
 *
 * @author jeffaschenk@gmail.com
 */
public abstract class TaskStatistic {

    protected Class<? extends RootElement> associatedClass;

    protected boolean processed = false;

    protected boolean done = false;

    protected boolean saved = false;

    protected Long existingRowCount = null;

    protected long startTime = 0;

    protected long stopTime = 0;

    protected int fileErrors;

    protected List<String> errorMessages = new ArrayList<String>();

    public Class<? extends RootElement> getAssociatedClass() {
        return associatedClass;
    }

    public void setAssociatedClass(Class<? extends RootElement> associatedClass) {
        this.associatedClass = associatedClass;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public Long getExistingRowCount() {
        return existingRowCount;
    }

    public void setExistingRowCount(Long existingRowCount) {
        this.existingRowCount = existingRowCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public long getDuration() {
        return this.getStopTime() - this.getStartTime();
    }

    public int getFileErrors() {
        return fileErrors;
    }

    public void setFileErrors(int fileErrors) {
        this.fileErrors = fileErrors;
    }

    public void incrementFileErrors() {
        this.fileErrors++;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}
