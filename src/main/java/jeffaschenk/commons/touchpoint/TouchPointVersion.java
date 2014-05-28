package jeffaschenk.commons.touchpoint;

/**
 * Provides Information regarding this Implementation which has
 * been baked into the MANIFEST of our JAR.
 *
 * @author jeffaschenk@gmail.com
 */
public class TouchPointVersion {
    private TouchPointVersion() {
    }

    /**
     * Get our Version
     *
     * @return  {@link String} object.
     */
    public static String getVersion() {
        return ((TouchPointVersion.class.getPackage().getImplementationVersion() == null) ? "UNKNOWN" : TouchPointVersion.class.getPackage().getImplementationVersion());
    }

    /**
     * Provide Full Version String
     *
     * @return String of with all Information.
     */
    public static String getFullVersionString() {
        return TouchPointVersion.class.getPackage().getImplementationTitle() + ", Version:[" + TouchPointVersion.getVersion() + "] by " +
                ((TouchPointVersion.class.getPackage().getImplementationVendor() == null) ? "UNKNOWN" : TouchPointVersion.class.getPackage().getImplementationVendor());
    }

    /**
     * Simply Main to Show Version Information.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(TouchPointVersion.getFullVersionString());
    }

}
