package jeffaschenk.commons.touchpoint.model;


import jeffaschenk.commons.identifiers.AlternateId;
import jeffaschenk.commons.system.internal.file.services.GlobalConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * RootElement
 * Abstract Class for common Inclusion by other Objects and Providing common root for all objects.
 *
 * @author Jeff Schenk
 * @version $Id: $
 */
@MappedSuperclass
public abstract class RootElement implements GlobalConstants, RootElementInterface, Cloneable, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private AlternateId alternateId;

    private boolean loaded = false;

    private boolean updated = false;

    private Date timestamp;

    protected RootElement() {
        this.id = new Integer(0);
        this.timestamp = new Date();
    }

    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //@EmbeddedId
    @Transient
    public AlternateId get_alt_id() {
        return alternateId;
    }

    public void set_alt_id(AlternateId alternateId) {
        this.alternateId = alternateId;
    }

    @Transient
    @Override
    public String getAlternateId() {
        return (this.alternateId == null) ? null : this.alternateId.getAlternateId();
    }

    @Override
    public void setAlternateId(String id) {
        if (this.alternateId == null) {
            this.alternateId = new AlternateId();
        }
        this.alternateId.setAlternateId(id);
    }

    @Override
    @Transient
    public boolean isExtractLoaded() {
        return loaded;
    }

    protected void setExtractLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    @Transient
    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    @Transient
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Default Global Pipe Separator Character String
     *
     * @return String
     */
    @Transient
    @Override
    public String getPipeCharacter() {
        return DEFAULT_GLOBAL_PIPE_CHARACTER;
    }

    /**
     * Obtain the String name of the JDBC table for the Object.
     *
     * @return String Name of the Table annotated on the Entity Object.
     */
    @Transient
    @Override
    public String getTableName() {
        Table tableAnnotation = this.getClass().getAnnotation(Table.class);
        return tableAnnotation == null ? null : tableAnnotation.name();
    }

    /**
     * helper method to perform parsing of the Extract File itself from
     */
    @Transient
    public static List<String> parseExtractFileRow(String input_line) {
        List<String> parsedInput = new ArrayList<String>();
        Scanner column_data = new Scanner(input_line).useDelimiter("\\" + DEFAULT_GLOBAL_PIPE_CHARACTER);
        while (column_data.hasNext()) {
            parsedInput.add(column_data.next());
        }
        column_data.close();
        return parsedInput;
    }

    /**
     * Export Object, only certain Objects will Implement
     * this method.
     *
     * @return String of UnMarshaled Object
     */
    @Transient
    @Override
    public String export() {
        return null;
    }

    /**
     * Helper to determine if Marshaled Element is Empty?
     *
     * @return boolean indicator, True if Empty for Export.
     */
    @Transient
    @Override
    public boolean isEmptyForExport() {
        return false;
    }

    /**
     * Helper method to obtain Export Header.
     * Normally, non will exit, only certain Objects
     * get exported.
     *
     * @return String Export Header
     */
    @Transient
    @Override
    public String getExportHeader() {
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RootElement other = (RootElement) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " ID:[" + ((this.id == null) ? "No ID yet" : this.id.toString()) + "]";
    }

    /**
     * Get the Pipe Header for the Object used upon export.
     *
     * @return String
     */
    @Transient
    @Override
    public String getPipeHeader() {
        // TODO
        return "|";
    }

    /**
     * Initialize object from a parsed Input Stream.
     *
     * @param parsedInput
     *
     */
    @Transient
    @Override
    public void initializeFromParsedExtract(List<String> parsedInput) {
        this.setExtractLoaded(true);
        // TODO -- And Extend As Needed in Implementing Classes.
    }

}
