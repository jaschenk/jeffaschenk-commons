package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

/**
 * @author jeff.schenk
 */
public class LDAPAttributeStatistic {

    public String STR_ATTRIBUTE_NAME_WITH_LARGEST_SIZE = "none";
    public long STR_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE = 0;

    public String BIN_ATTRIBUTE_NAME_WITH_LARGEST_SIZE = "none";
    public long BIN_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE = 0;

    public long TOTAL_NUMBER_OF_STR_ATTRIBUTES = 0;
    public long TOTAL_NUMBER_OF_BIN_ATTRIBUTES = 0;
    public long TOTAL_NUMBER_OF_ATTRIBUTES = 0;
    public long TOTAL_SIZE_OF_ALL_ATTRIBUTES = 0;
    public long TOTAL_IGNORED_ATTRIBUTES = 0;

    /**
     * Creates a new instance of LDAPAttributeStatistic
     */
    public LDAPAttributeStatistic() {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Total# Attrs:[" + TOTAL_NUMBER_OF_ATTRIBUTES + "] ");
        sb.append("Total Data Size:[" + TOTAL_SIZE_OF_ALL_ATTRIBUTES + "] ");

        if (TOTAL_NUMBER_OF_STR_ATTRIBUTES > 0) {
            sb.append("Total# Str Attrs:[" + TOTAL_NUMBER_OF_STR_ATTRIBUTES + "] ");
            sb.append("MAX Str Attr:[" + STR_ATTRIBUTE_NAME_WITH_LARGEST_SIZE + "] ");
            sb.append("MAX Str Size:[" + STR_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE + "] ");
        }

        if (TOTAL_NUMBER_OF_BIN_ATTRIBUTES > 0) {
            sb.append("Total# Bin Attrs:[" + TOTAL_NUMBER_OF_BIN_ATTRIBUTES + "] ");
            sb.append("MAX Bin Attr:[" + BIN_ATTRIBUTE_NAME_WITH_LARGEST_SIZE + "] ");
            sb.append("MAX Bin Size:[" + BIN_ATTRIBUTE_WITH_LARGEST_ATTRIBUTE_SIZE + "] ");
        }

        if (TOTAL_IGNORED_ATTRIBUTES > 0) {
            sb.append("Total Ignored:[" + TOTAL_IGNORED_ATTRIBUTES + "]");
        }

        // **************************
        // return String.
        return sb.toString();
    } // End of toString Method OverRide.

} ///:~ End of LDAPAttributeStatistic
