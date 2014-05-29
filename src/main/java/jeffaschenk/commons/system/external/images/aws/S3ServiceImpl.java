package jeffaschenk.commons.system.external.images.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import jeffaschenk.commons.environment.SpringAccessor;
import jeffaschenk.commons.environment.SystemEnvironmentBean;
import jeffaschenk.commons.types.EnvironmentType;
import jeffaschenk.commons.types.ImageServiceType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * AWS S3 Bucket Service Implementation to provide storage for
 * Application Images.
 */
public class S3ServiceImpl implements S3Service {
    private final static Log logger = LogFactory.getLog(S3ServiceImpl.class);


    /**
     * Common S3 Client Object
     */
    private boolean deferredClientConnection = false;
    private AmazonS3Client s3;

    /**
     * AWS S3 Bucket Name for Auction an Item Images.
     */
    private String primaryBucket;

    /**
     * Calculated Expiration of Images
     */
    private Date imageExpiration;

    /**
     * AWS S3 Bucket for all User Avatar Images, to provide some separation between
     * Auction and Item Images and User Images.
     */
    private String avatarBucket;

    /**
     * Calculated Expiration of Images
     */
    private Date avatarExpiration;

    /**
     * Default Constructor, Injected via Spring Configuration
     * to use AWS Services.
     *
     * @param accessKey
     * @param secretKey
     * @param bucket
     */
    public S3ServiceImpl(String runningImageService, String accessKey, String secretKey, String bucket, String avatarBucket,
                         int connectionTimeOut, int maxRetries) {

        // If we are running in the ESB, do not start this Service.
        SystemEnvironmentBean systemEnvironment = SpringAccessor.getSystemEnvironmentBean();
        if (systemEnvironment.runningEnvironmentType() == EnvironmentType.ESB)
        {
            this.deferredClientConnection = true;
            this.s3 = null;
            logger.info("ESB Environment Detected, Amazon Web Services (AWS) S3 Bucket Service Disabled.");
        } else {

        // We are not in ESB so, boot the service.
        if (runningImageService.equalsIgnoreCase(ImageServiceType.S3Service.toString())) {

            logger.info("Initializing Amazon Web Services (AWS) S3 Bucket Service Implementation.");
            this.primaryBucket = bucket;
            this.avatarBucket = avatarBucket;
            // ****************************************
            // Calculate the Expiration of Objects
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, 1, 1);
            imageExpiration = calendar.getTime();
            // ****************************************
            // Calculate the Expiration of Objects
            avatarExpiration = calendar.getTime();

            // **********************************
            // Obtain AWS S3 Client.
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            clientConfiguration.setConnectionTimeout(connectionTimeOut);
            clientConfiguration.setMaxErrorRetry(maxRetries);
            s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey), clientConfiguration);

            // Indicate our Bucket Usage
            logger.info("AWS S3 Primary Avatar Bucket:[" + avatarBucket + "]");
            logger.info("AWS S3 Primary Image Bucket:[" + bucket + "]");
            // Indicate Configuration
            logger.info("AWS S3 Connection Time Out:[" + clientConfiguration.getConnectionTimeout() + "], Max Retries:[" +
                    clientConfiguration.getMaxErrorRetry() + "]");
            // **********************************
            // Attempt to
            // List Available Buckets and ensure
            // they have correct Read permissions.
            try {
                List<Bucket> availableBuckets = s3.listBuckets();
                Iterator<Bucket> itr = availableBuckets.iterator();
                while (itr.hasNext()) {
                    Bucket aBucket = itr.next();
                    logger.info("  + (AWS)S3 Bucket available:[" + aBucket.getName() + "], setting Read Permissions.");
                    AccessControlList accessControlList = s3.getBucketAcl(aBucket.getName());
                    accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                    s3.setBucketAcl(aBucket.getName(), accessControlList);
                }
            } catch (AmazonClientException ace) {
                this.deferredClientConnection = true;
                logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
            }

            // Done, Ready.
            if (this.deferredClientConnection) {
                logger.info("Amazon Web Services (AWS) S3 Bucket Service Implementation Initialization Deferred.");
            } else {
                logger.info("Amazon Web Services (AWS) S3 Bucket Service Implementation Successfully Initialized.");
            }

        }
        }
    }

    @Override
    public boolean isDeferredClientConnection() {
        return deferredClientConnection;
    }

    @Override
    public ImageServiceType getImageServiceType() {
        return ImageServiceType.S3Service;
    }

    @Override
    public URL generateResourceURL(String key) {
        if (s3 == null)
            { return null; }
        try {
            return s3.generatePresignedUrl(primaryBucket, key, imageExpiration);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
        throw new RuntimeException("Unable to Obtain Pre-signed Url for Image.");
    }

    @Override
    public URL generateAvatarURL(String key) {
        if (s3 == null)
            { return null; }
        try {
            return s3.generatePresignedUrl(avatarBucket, key, avatarExpiration);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
        throw new RuntimeException("Unable to Obtain Pre-signed Url for Avatar.");
    }

    @Override
    public String[] createImage(String sid, InputStream imageContent, String contentType, long size,
                                boolean createThumbnail) {
        if (s3 == null)
            { return new String[2]; }
        try {
            return saveContent(this.primaryBucket, sid, imageContent, contentType, size, createThumbnail);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
        throw new RuntimeException("Unable to Create Image.");
    }

    @Override
    public void deleteImage(String filename) {
        if (s3 == null)
            { return; }
        try {
            s3.deleteObject(primaryBucket, filename);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
    }

    @Override
    public String[] createAvatar(String sid, InputStream imageContent, String contentType, long size,
                                 boolean createThumbnail) {
        if (s3 == null)
            { return new String[2]; }
        try {
            return saveContent(this.avatarBucket, sid, imageContent, contentType, size, createThumbnail);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
        throw new RuntimeException("Unable to Create Avatar.");
    }

    @Override
    public void deleteAvatar(String filename) {
        if (s3 == null)
            { return; }
        try {
            s3.deleteObject(primaryBucket, filename);
        } catch (AmazonClientException ace) {
            this.deferredClientConnection = true;
            logger.error("Attempting Access to (AWS)S3 has failed:[" + ace.getMessage() + "], Cause:[" + ace.getCause() + "].");
        }
    }

    /**
     * Private Helper Method to Save Avatar or Images to a specific Bucket.
     *
     * @param storageBucket
     * @param sid
     * @param imageContent
     * @param contentType
     * @param size
     * @param createThumbnail
     * @return String[]
     */
    private String[] saveContent(String storageBucket, String sid, InputStream imageContent, String contentType, long size,
                                 boolean createThumbnail) throws AmazonClientException {
        String[] fileNames = new String[2];
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);
        objectMetadata.setLastModified(new Date());
        String filename = sid + ".jpg";

        // Save the Content
        try {
            ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream(imageContent.available());
            IOUtils.copy(imageContent, tempBuffer);
            byte[] image = tempBuffer.toByteArray();

            // Store Image
            s3.putObject(storageBucket, filename, new ByteArrayInputStream(image), objectMetadata);
            fileNames[0] = filename;
            // Grant Access Rights to All users.
            AccessControlList accessControlList = s3.getObjectAcl(storageBucket, fileNames[0]);
            accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
            s3.setObjectAcl(storageBucket, fileNames[0], accessControlList);

            // Thumbnail to be saved?
            if (createThumbnail) {

                /**
                // Add thumbnail
                byte[] thumbnail = ImageUtils.createThumbnailImage(new ByteArrayInputStream(image), null);
                ObjectMetadata thumbnailMetadata = new ObjectMetadata();
                thumbnailMetadata.setContentLength(thumbnail.length);
                thumbnailMetadata.setContentType(contentType);
                thumbnailMetadata.setLastModified(new Date());
                // Store Thumbnail
                s3.putObject(storageBucket, sid + "t.jpg", new ByteArrayInputStream(thumbnail), thumbnailMetadata);
                fileNames[1] = sid + "t.jpg";
                // Grant Access Rights to All users.
                accessControlList = s3.getObjectAcl(storageBucket, fileNames[1]);
                accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);
                s3.setObjectAcl(storageBucket, fileNames[1], accessControlList);
                **/

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileNames;
    }

}
