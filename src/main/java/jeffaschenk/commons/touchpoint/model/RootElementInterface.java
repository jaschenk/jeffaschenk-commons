package jeffaschenk.commons.touchpoint.model;

import java.util.Date;
import java.util.List;

/**
 * Common Interface for JnJ Elements.
 */
public interface RootElementInterface {

    /**
     * Determine if the object was loaded from Extract or not.
     * If true, Object Marshaled from Extract File, if False, default,
     * marshaled from persistent store orby other means.
     *
     * @return boolean indicator per above.
     */
    public boolean isExtractLoaded();

    /**
     * Update indicator, true if update to persistent store required.
     *
     * @return boolean indicator
     */
    public boolean isUpdated();

    /**
     * Set Update Indicator
     *
     * @param updated
     */
    public void setUpdated(boolean updated);

    /**
     * Get Timestamp of current Update.
     *
     * @return Date
     */
    public Date getTimestamp();

    /**
     * Default Global Pipe Separator Character String
     *
     * @return String
     */
    public String getPipeCharacter();

    /**
     * Provide specific Alt ID for this Object.
     * Wrapper for accessing Embeddable Alt Id within
     * Primary composite Keys.
     *
     * @return String of alternateId
     */
    public String getAlternateId();

    /**
     * Provide common setter for specific Alt ID on this Object.
     * Wrapper for accessing Embeddable Alt Id within
     * Primary composite Keys.
     *
     * @param alternateId
     */
    public void setAlternateId(String alternateId);

    /**
     * Obtain the String name of the JDBC table for the Object.
     *
     * @return String
     */
    public String getTableName();

    /**
     * Get the Pipe Header for the Object used upon export.
     *
     * @return  String
     */
    public String getPipeHeader();


    /**
     * Initialize object from a parsed Input Stream.
     *
     * @param parsedInput
     *
     */
    public void initializeFromParsedExtract(List<String> parsedInput);

    /**
     * Export Object
     *
     * @return String of UnMarshaled Object
     */
    public String export();

    /**
     * Helper to determine if Marshaled Element is Empty?
     *
     * @return boolean indicator, True if Element Empty.
     */
    public boolean isEmptyForExport();

    /**
     * Get Export Header for Class
     * @return  String
     */
     public String getExportHeader();

}
