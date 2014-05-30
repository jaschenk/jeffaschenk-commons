package jeffaschenk.commons.touchpoint.model;

/**
 * Initial Simple Model Version Object
 * Can be seeded using Spring Beans
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class ModelVersion {

	private final static String modelVersion = "1.0.0";
	private final static String modelTimeStamp = "May 30, 2014 12:18:01 PM PDT";
	
	/**
	 * Private Constructor
	 */
	private ModelVersion() {
	}

	/**
	 * <p>Getter for the field <code>modelVersion</code>.</p>
	 *
	 * @return a {@link String} object.
	 */
	public static String getModelVersion() {
		return modelVersion;
	}

	/**
	 * <p>Getter for the field <code>modelTimeStamp</code>.</p>
	 *
	 * @return a {@link String} object.
	 */
	public static String getModelTimeStamp() {
		return modelTimeStamp;
	}

    /**
     * Provide Full Version String
     * @return String of with all Information.
     */
    public static String getFullVersionString() {
        return "Touchpoint Model Version:[" + ModelVersion.modelVersion +"], TimeStamp:[" + ModelVersion.modelTimeStamp + "]";
    }

    /**
     * Simply Main to Show Version Information.
     * @param args
     */
    public static void main(String[] args){
        System.out.println(ModelVersion.getFullVersionString());
    }

}
