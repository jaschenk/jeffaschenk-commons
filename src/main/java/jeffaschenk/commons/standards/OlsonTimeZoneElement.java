package jeffaschenk.commons.standards;

import java.math.BigInteger;

public class OlsonTimeZoneElement implements java.io.Serializable {

    private static final long serialVersionUID = 1109L;

    private BigInteger id;
    private String utc;
    private String utcsummer;
    private String natoalpha;
    private String natophonetic;
    private String timezonename;
    private String countryCode;
    private String comments;

    public OlsonTimeZoneElement() {
    }

    /**
     * Full Constructor for use within Memory
     * based Contstant.
     *
     * @param tzid
     * @param utc
     * @param utcsummer
     * @param natoalpha
     * @param natophonetic
     * @param timezonename
     * @param countryCode
     * @param comments
     */
    public OlsonTimeZoneElement(int tzid,
                                String utc,
                                String utcsummer,
                                String natoalpha,
                                String natophonetic,
                                String timezonename,
                                String countryCode,
                                String comments) {
        this.setId(new BigInteger(Integer.toString(tzid)));
        this.utc = utc;
        this.utcsummer = utcsummer;
        this.natoalpha = natoalpha;
        this.natophonetic = natophonetic;
        this.timezonename = timezonename;
        this.countryCode = countryCode;
        this.comments = comments;
    }


    public BigInteger getId() {
        return this.id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getUtc() {
        return this.utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getUtcsummer() {
        return this.utcsummer;
    }

    public void setUtcsummer(String utcsummer) {
        this.utcsummer = utcsummer;
    }

    public String getNatoalpha() {
        return this.natoalpha;
    }

    public void setNatoalpha(String natoalpha) {
        this.natoalpha = natoalpha;
    }

    public String getNatophonetic() {
        return this.natophonetic;
    }

    public void setNatophonetic(String natophonetic) {
        this.natophonetic = natophonetic;
    }

    public String getTimezonename() {
        return this.timezonename;
    }

    public void setTimezonename(String timezonename) {
        this.timezonename = timezonename;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String country) {
        this.countryCode = countryCode;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
