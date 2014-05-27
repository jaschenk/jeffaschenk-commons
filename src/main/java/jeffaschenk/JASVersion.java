package jeffaschenk;

/**
 * Provides Information regarding this Implementation which has
 * been baked into the MANIFEST of our JAR.
 *
 * @author jeffaschenk@gmail.com
 */
public class JASVersion {
    private JASVersion() {
    }

    /**
     * Get our Version
     *
     * @return a {@link String} object.
     */
    public static String getVersion() {
        return ((JASVersion.class.getPackage().getImplementationVersion() == null) ? "UNKNOWN" : JASVersion.class.getPackage().getImplementationVersion());
    }

    /**
     * Provide Full Version String
     *
     * @return String of with all Information.
     */
    public static String getFullVersionString() {
        return JASVersion.class.getPackage().getImplementationTitle() + ", Version:[" + JASVersion.getVersion() + "] by " +
                ((JASVersion.class.getPackage().getImplementationVendor() == null) ? "UNKNOWN" : JASVersion.class.getPackage().getImplementationVendor());
    }

    /**
     * Simply Main to Show Version Information.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(JASVersion.getFullVersionString());
    }

}
