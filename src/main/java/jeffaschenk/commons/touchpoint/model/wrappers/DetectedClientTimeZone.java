package jeffaschenk.commons.touchpoint.model.wrappers;

import jeffaschenk.commons.util.StringUtils;

/**
 * Provides wrapper Object to contain the
 * detected TimeZone Information from the Client device.
 *
 * @author jeffaschenk@gmail.com
 */
public class DetectedClientTimeZone implements java.io.Serializable {
    private static final long serialVersionUID = 1109L;

    private String utcOffset;

    private String zoneInfoKey;  // Olson Timezone DB Name

    private boolean usesDST = false;

    /**
     * Default Constructor
     */
     public DetectedClientTimeZone() {
    }

    /**
     * Constructor with all relative Data Parsed.
     *
     * @param utcOffset
     * @param zoneInfoKey
     * @param usesDST
     */
    public DetectedClientTimeZone(String utcOffset, String zoneInfoKey, boolean usesDST) {
        this.utcOffset = utcOffset;
        this.zoneInfoKey = zoneInfoKey;
        this.usesDST = usesDST;
    }


    /**
     * Constructor with only rendered HTML to be parses into Fields.
     * Example Obtained Data:
     * <b>UTC-offset</b>: -08:00<br/><b>Zoneinfo key</b>: America/Los_Angeles<br/><b>Zone uses DST</b>: yes<br/>
     *
     * @param renderedHTML
     */
    public DetectedClientTimeZone(String renderedHTML) {
        if (StringUtils.isEmpty(renderedHTML))
            { return; }
        //
        // Please note this parser is content sensitive.
        // Any changes to original detect_timezone.js will need to be addressed
        // here as well.
        //
        String[] tokenArray = renderedHTML.split("<br/>");
        for(String token : tokenArray)
        {
             if (StringUtils.isEmpty(token))
                { continue; }
             if (token.startsWith("<b>"))
                { token = token.substring(3); }
             token = token.replace("</b>","");
             String[] tokenValue = token.split(":",2);
            if ( (tokenValue == null) ||
                 (tokenValue.length != 2) ||
                 (tokenValue[0] == null) ||
                 (tokenValue[1] == null) )
                { continue; }
             tokenValue[0] = tokenValue[0].trim();
             tokenValue[1] = tokenValue[1].trim();
             // Now determine What Parameter we have to Save.
             if (tokenValue[0].equalsIgnoreCase("UTC-offset"))
             {
                   this.setUtcOffset(tokenValue[1]);
             } else if (tokenValue[0].equalsIgnoreCase("Zoneinfo key"))
             {
                    this.setZoneInfoKey(tokenValue[1]);
             } else if (tokenValue[0].equalsIgnoreCase("Zone uses DST"))
             {
                    this.setUsesDST( (tokenValue[1].equalsIgnoreCase("yes")) ? true : false);
             } else {
                 // For now throw unknown data out the door.....
             }
        } // End of For Each Parsing Loop.
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getZoneInfoKey() {
        return zoneInfoKey;
    }

    public void setZoneInfoKey(String zoneInfoKey) {
        this.zoneInfoKey = zoneInfoKey;
    }

    public boolean isUsesDST() {
        return usesDST;
    }

    public void setUsesDST(boolean usesDST) {
        this.usesDST = usesDST;
    }

    @Override
    public String toString() {
        return "DetectedClientTimeZone{" +
                "utcOffset='" + utcOffset + '\'' +
                ", zoneInfoKey='" + zoneInfoKey + '\'' +
                ", usesDST=" + usesDST +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetectedClientTimeZone that = (DetectedClientTimeZone) o;

        if (usesDST != that.usesDST) return false;
        if (utcOffset != null ? !utcOffset.equals(that.utcOffset) : that.utcOffset != null) return false;
        if (zoneInfoKey != null ? !zoneInfoKey.equals(that.zoneInfoKey) : that.zoneInfoKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = utcOffset != null ? utcOffset.hashCode() : 0;
        result = 31 * result + (zoneInfoKey != null ? zoneInfoKey.hashCode() : 0);
        result = 31 * result + (usesDST ? 1 : 0);
        return result;
    }
}
