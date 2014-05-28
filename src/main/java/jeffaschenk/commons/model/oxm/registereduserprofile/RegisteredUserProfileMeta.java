package jeffaschenk.commons.model.oxm.registereduserprofile;

import jeffaschenk.commons.model.wrappers.DetectedClientTimeZone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * RegisteredUserProfileMeta
 * JAVA Pojo representation of the contents of the <code>RegisteredUserProfile</code> "meta" field/property.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 *         Date: April 27, 2011
 */
@XmlRootElement(name = "meta")
public class RegisteredUserProfileMeta implements java.io.Serializable {
    private static final long serialVersionUID = 1109L;

    private DetectedClientTimeZone detectedClientTimeZone;

    // Add Additional Data Structures as needed for MetaData contents

    /**
     * Default Constructor
     */
    public RegisteredUserProfileMeta() {
        detectedClientTimeZone = new DetectedClientTimeZone();
    }

    @XmlElement(name="detectedClientTimeZone")
    public DetectedClientTimeZone getDetectedClientTimeZone() {
        return detectedClientTimeZone;
    }

    public void setDetectedClientTimeZone(DetectedClientTimeZone detectedClientTimeZone) {
        this.detectedClientTimeZone = detectedClientTimeZone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisteredUserProfileMeta that = (RegisteredUserProfileMeta) o;

        if (detectedClientTimeZone != null ? !detectedClientTimeZone.equals(that.detectedClientTimeZone) : that.detectedClientTimeZone != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return detectedClientTimeZone != null ? detectedClientTimeZone.hashCode() : 0;
    }
}
