package jeffaschenk.commons.system.external.images.aws;

import jeffaschenk.commons.types.ImageServiceType;

import java.io.InputStream;
import java.net.URL;

/**
 * AWS S3 Service Extending Base Image Services.
 *
 * @author  jeffaschenk@gmail.com
 *
 */
public interface S3Service {

    public URL generateResourceURL(String key);

    public URL generateAvatarURL(String key);

    ImageServiceType getImageServiceType();

    /**
     * Allows for Indication to Upstream component
     * that we have a deferred connection and Operations
     * could fail.
     *
     * @return boolean
     */
    boolean isDeferredClientConnection();

    /**
     * Creates an image on the NAS or other implemented component.
     *
     * @param SID the SID of the object that should be associated with the image
     * @param imageContent the actual image
     * @param contentType The mime type of the image, should be retrieved from the upload mechanism
     * @param size the size of the input stream in bytes
     * @param createThumbnail should a thumbnail also be created? If false, the returned String[] will only contain
     * one element.
     * @return the filename that was generated during the upload. This should be stored as a reference. filenames[0] is
     * the full image name and filenames[1] is the thumbnail.
     */
    String[] createImage(String SID, InputStream imageContent, String contentType, long size, boolean createThumbnail);


    /**
     * Deletes an image from the NAS or other implemented component.
     * @param filename the name of the file that should be deleted. Must be relative to the base folder on the NAS
     */
    void deleteImage(String filename);

    /**
     *  Create Avatar on the NAS or other implemented component.
     *
     * @param sid
     * @param imageContent
     * @param contentType
     * @param size
     * @param createThumbnail
     * @return String[]
     */
    String[] createAvatar(String sid, InputStream imageContent, String contentType, long size, boolean createThumbnail);

    /**
     * Deletes an Avatar from the NAS or other implemented component.
     * @param filename
     */
    void deleteAvatar(String filename);



}
