package jeffaschenk.commons.touchpoint.model.transitory;

import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.util.TimeDuration;
import jeffaschenk.commons.util.TimeUtils;

import java.io.File;

/**
 * Simple Transitory Task Object, which represent the
 * Import Task being performed and Queued within a Thread Pool.
 *
 * @author jeffaschenk@gmail.com
 */
public class ImportLoadStatistic extends TaskStatistic implements GlobalConstants {

    private File extractFile;
    
    private String networkFileName;

    private boolean fileViaSecureNetworkChannel = false;

    private boolean performArchive = false;

    private boolean fileSliced = false;

    private String pipeHeader;

    private boolean archived = false;

    private int rowsInError = 0;

    private int rowsSkippedNoID = 0;

    private int rowsProcessed = 0;

    private int rowsSkippedHeader = 0;

    private int rowsSkipped = 0;

    private int rowsReadFromFile = 0;

    private int rowsSkippedDueToPendingUpdates = 0;


    /**
     * Default Constructor
     *
     * @param extractFile
     * @param associatedClass
     */
    public ImportLoadStatistic(File extractFile, Class<? extends RootElement> associatedClass, Long existingRowCount) {
        this.setExtractFile(extractFile);
        this.associatedClass = associatedClass;
        this.existingRowCount = existingRowCount;
        this.setStartTime(TimeUtils.now());
        this.setStopTime(this.getStartTime());

    }

    public String getExtractFileName() {
        return this.extractFile.getName();
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isFileSliced() {
        return fileSliced;
    }

    public void setFileSliced(boolean fileSliced) {
        this.fileSliced = fileSliced;
    }

    public int getRowsProcessed() {
        return rowsProcessed;
    }

    public void setRowsProcessed(int rowsProcessed) {
        this.rowsProcessed = rowsProcessed;
    }

    public void incrementRowsProcessed() {
        this.rowsProcessed++;
    }

    public int getRowsSkipped() {
        return rowsSkipped;
    }

    public void setRowsSkipped(int rowsSkipped) {
        this.rowsSkipped = rowsSkipped;
    }

    public void incrementRowsSkipped() {
        this.rowsSkipped++;
    }

    public int getRowsInError() {
        return rowsInError;
    }

    public void setRowsInError(int rowsInError) {
        this.rowsInError = rowsInError;
    }

    public void incrementRowsInError() {
        this.rowsInError++;
    }

    public int getRowsReadFromFile() {
        return rowsReadFromFile;
    }

    public void setRowsReadFromFile(int rowsReadFromFile) {
        this.rowsReadFromFile = rowsReadFromFile;
    }

    public void incrementRowsReadFromFile() {
        this.rowsReadFromFile++;
    }

    public String getPipeHeader() {
        return pipeHeader;
    }

    public void setPipeHeader(String pipeHeader) {
        this.pipeHeader = pipeHeader;
    }

    public int getRowsSkippedNoID() {
        return rowsSkippedNoID;
    }

    public void setRowsSkippedNoID(int rowsSkippedNoID) {
        this.rowsSkippedNoID = rowsSkippedNoID;
    }

    public void incrementRowsSkippedNoID() {
        this.rowsSkippedNoID++;
    }

    public int getRowsSkippedHeader() {
        return rowsSkippedHeader;
    }

    public void setRowsSkippedHeader(int rowsSkippedHeader) {
        this.rowsSkippedHeader = rowsSkippedHeader;
    }

    public void incrementRowsSkippedHeader() {
        this.rowsSkippedHeader++;
    }

    public int getRowsSkippedDueToPendingUpdates() {
        return rowsSkippedDueToPendingUpdates;
    }

    public void setRowsSkippedDueToPendingUpdates(int rowsSkippedDueToPendingUpdates) {
        this.rowsSkippedDueToPendingUpdates = rowsSkippedDueToPendingUpdates;
    }

    public void incrementRowsSkippedDueToPendingUpdates() {
        this.rowsSkippedDueToPendingUpdates++;
    }

    public File getExtractFile() {
        return extractFile;
    }

    public void setExtractFile(File extractFile) {
        this.extractFile = extractFile;
        if ( (extractFile != null) && (extractFile.getName().contains(FILE_SLICE)) ) {
                    this.setFileSliced(true);
        }
    }

    public boolean isPerformArchive() {
        return performArchive;
    }

    public void setPerformArchive(boolean performArchive) {
        this.performArchive = performArchive;
    }

    public boolean isFileViaSecureNetworkChannel() {
        return fileViaSecureNetworkChannel;
    }

    public void setFileViaSecureNetworkChannel(boolean fileViaSecureNetworkChannel) {
        this.fileViaSecureNetworkChannel = fileViaSecureNetworkChannel;
    }

    public String getNetworkFileName() {
        return networkFileName;
    }

    public void setNetworkFileName(String networkFileName) {
        this.networkFileName = networkFileName;
    }

    @Override
    public String toString() {
        return "ExtractLoadStatistic{" +
                NEWLINE +"extractFile=" + ( (extractFile != null) ? extractFile.getName().toString() : "none") +
                NEWLINE +", networkFileName=" + ( (networkFileName != null) ? networkFileName : "none") +
                NEWLINE + ", associatedClass=" + associatedClass.getSimpleName() +
                NEWLINE + ", fileViaSecureNetworkChannel=" + fileViaSecureNetworkChannel +
                NEWLINE + ", processed=" + processed +
                NEWLINE + ", archived=" + archived +
                NEWLINE + ", sliced=" + fileSliced +
                NEWLINE + ", performArchive=" + performArchive +
                NEWLINE + ", done=" + done +
                NEWLINE + ", saved=" + saved +
                NEWLINE + ", existingRowCount=" + existingRowCount +
                NEWLINE + ", rowsReadFromFile=" + rowsReadFromFile +
                NEWLINE + ", rowsProcessed=" + rowsProcessed +
                NEWLINE + ", rowsSkipped=" + rowsSkipped +
                NEWLINE + ", rowsSkippedDueToPendingUpdates=" + rowsSkippedDueToPendingUpdates +
                NEWLINE + ", rowsSkippedHeader=" + rowsSkippedHeader +
                NEWLINE + ", rowsSkippedNoID=" + rowsSkippedNoID +
                NEWLINE + ", rowsInError=" + rowsInError +
                NEWLINE + ", fileErrors=" + fileErrors +
                NEWLINE + " Duration=" + TimeDuration.getElapsedtoString(this.getDuration()) +
                NEWLINE + ((errorMessages.size() > 0) ? ", errorMessages=" + errorMessages.toString() + NEWLINE + " }" : " }");
    }
}
