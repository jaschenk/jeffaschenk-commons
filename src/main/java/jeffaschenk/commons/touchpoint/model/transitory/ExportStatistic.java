package jeffaschenk.commons.touchpoint.model.transitory;

import jeffaschenk.commons.system.internal.file.services.GlobalConstants;
import jeffaschenk.commons.touchpoint.model.RootElement;
import jeffaschenk.commons.util.TimeDuration;
import jeffaschenk.commons.util.TimeUtils;

import java.io.File;

/**
 * Simple Transitory Task Object, which represent the
 * Export Task being performed and Queued within a Thread Pool.
 *
 * @author jeffaschenk@gmail.com
 */
public class ExportStatistic extends TaskStatistic implements GlobalConstants {

    private File exportFile;

    private int rowsProcessed = 0;

    private int rowsHeader = 0;

    private int rowsWrittenToFile = 0;


    /**
     * Default Constructor
     *
     * @param exportFile
     * @param associatedClass
     */
    public ExportStatistic(File exportFile, Class<? extends RootElement> associatedClass, Long existingRowCount) {
        this.exportFile = exportFile;
        this.associatedClass = associatedClass;
        this.existingRowCount = existingRowCount;
        this.setStartTime(TimeUtils.now());
        this.setStopTime(this.getStartTime());
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

    public int getRowsWrittenToFile() {
        return this.rowsWrittenToFile;
    }

    public void setRowsWrittenToFile(int rowsWrittenToFile) {
        this.rowsWrittenToFile = rowsWrittenToFile;
    }

    public void incrementRowsWrittenToFile() {
        this.rowsWrittenToFile++;
    }

    public int getRowsHeader() {
        return rowsHeader;
    }

    public void setRowsHeader(int rowsHeader) {
        this.rowsHeader = rowsHeader;
    }

    public void incrementRowsHeader() {
        this.rowsHeader++;
    }

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }

    @Override
    public String toString() {
        return "ExportStatistic{" +
                NEWLINE +"exportFile=" + exportFile.getName().toString() +
                NEWLINE + ", associatedClass=" + associatedClass.getSimpleName() +
                NEWLINE + ", processed=" + processed +
                NEWLINE + ", done=" + done +
                NEWLINE + ", saved=" + saved +
                NEWLINE + ", existingRowCount=" + existingRowCount +
                NEWLINE + ", rowsWrittenToFile=" + rowsWrittenToFile +
                NEWLINE + ", rowsProcessed=" + rowsProcessed +
                NEWLINE + ", rowsHeader=" + rowsHeader +
                NEWLINE + ", fileErrors=" + fileErrors +
                NEWLINE + " Duration=" + TimeDuration.getElapsedtoString(this.getDuration()) +
                NEWLINE + ((errorMessages.size() > 0) ? ", errorMessages=" + errorMessages.toString() + NEWLINE + " }" : " }");
    }
}
