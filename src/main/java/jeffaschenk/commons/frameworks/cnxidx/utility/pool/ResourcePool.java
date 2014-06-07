package jeffaschenk.commons.frameworks.cnxidx.utility.pool;

import jeffaschenk.commons.exceptions.NoFreeResourcesException;
import jeffaschenk.commons.exceptions.PoolNotAvailableException;
import jeffaschenk.commons.exceptions.ResourceProviderException;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Provides access to a pool of scarce resources.  Typically,
 * resources will be "connection" type resources.  However, this
 * pool can be used for any scarce or expensive to construct resources
 * that applications need reoccuring access to. Resources placed into this
 * pool must implement the ResourceProvider interface.
 * <p> The ResourcePool uses several different properties which are passed
 * in to the constructor as TypedProperties.  These properties vary
 * depending on the scarce resource being managed.
 * <p> The resource pool is implemented using two vectors. One vector represents
 * the "free" or available resources and the other vector represents the "in use"
 * resources. Thus, when a client obtains a resource from this pooling mechanism,
 * the resource is moved from the free pool into the used pool.
 * <p> It is IMPORTANT that clients "free" the resources after they are done using
 * them. Otherwise, there will be no resources available to other clients.
 */
public class ResourcePool {
/*
 * Kro - 2001/09/12 - added the ability to lock the pool.
 */

    /**
     * Indicates not to wait if no resources are available.
     */
    static public final int NO_WAIT = -1;

    /**
     * Indicates to wait until a resources becomes available if none are already available.
     */
    static public final int WAIT_FOREVER = 0;

    /**
     * Identifies the pool's state.
     */
    private boolean locked;

    /**
     * Why the pool has been locked - placed in exception messages
     * when locked is true.
     */
    private String lockReason = null;

    /**
     * Collects resources that are created and available
     */
    private Vector<Object> freeResources = new Vector<>();

    /**
     * Collects resources that are created and currently in use
     */
    private Vector<Object> usedResources = new Vector<>();

    /**
     * The name of the owner of this pool instance
     */
    private String owner;

    /**
     * The minimum number of resources this pool should ever have
     */
    private int min;

    /**
     * The maximum number of resources this pool should ever have
     */
    private int max;

    /**
     * Default wait period for obtaining a resource from the free pool
     */
    private long timeout;

    /**
     * provides lifecycle services for resources in the pool
     */
    private ResourceProvider rp;

    /**
     * provides lifecycle services for resources in the pool
     */
    private Properties props;

    /**
     * determins if the resources are validated on each retrieval from the pool
     * for use or not
     */
    protected boolean resourceValidation = false;

    /**
     * The constructor takes the name of the pool owner, a properties list
     * that will be used by the ResourcePool and possibly the ResourceProvider
     * as well to construct resources.
     *
     * @param owner      The owner of the pool
     * @param min        Minimum resources to initialize the pool with.
     * @param max        Maximum resources to initialize the pool with.
     * @param timeout    The time to wait for a resource to become available.
     * @param rp         The resource provider object which provides lifecycle
     *                   services for the resource
     * @param validation set to true if we should validate the resource each time
     *                   we get one.  False will never validate them.
     */
    public ResourcePool(String owner, int min, int max, int timeout,
                        ResourceProvider rp, boolean validation) {

        this.owner = owner;
        this.min = min;
        this.max = max;
        this.timeout = timeout;
        this.rp = rp;

        this.locked = false;

        this.resourceValidation = validation;

        // Initialize the pool
        initPool();
    }

    /**
     * The constructor takes the name of the pool owner, a properties list
     * that will be used by the ResourcePool and possibly the ResourceProvider
     * as well.
     *
     * @param owner The owner of the pool.
     * @param props Properties which are needed to create the pool or the ResourceProvider.
     * @param rp    The resource provider object which provides lifecycle services for the resource.
     */
    public ResourcePool(String owner, Properties props,
                        ResourceProvider rp) {

        this.owner = owner;
        this.min = Integer.parseInt((String)props.get("min"));  // Default 1
        this.max = Integer.parseInt((String)props.get("max"));  // Default 1
        this.timeout = Integer.parseInt((String)props.get("timeout")); // Default 0
        this.rp = rp;
        this.props = props;

        this.locked = false;

        initPool();
    }

    /**
     * Obtain a pooled resource, but only wait the default number
     * of seconds to obtain that resource.  The default number of
     * seconds to wait for a resources is retrieve from the properties
     * object passed into the constructor.  The property name is 'timeout'.
     * <p> Remember to release or free the resource after using it.
     *
     * @return Object the resource acquired from the pool.
     * @throws jeffaschenk.commons.exceptions.NoFreeResourcesException Thrown when there are no resources available.
     */
    public synchronized Object acquireResource()
            throws NoFreeResourcesException {
        return acquireResource(this.timeout * 1000);
    }

    /**
     * Obtain a pooled resource, but only wait the specified
     * number of milliseconds to obtain that resource.  If you
     * want to wait "forever" until a resource is availble, then
     * pass in 0.  If you don't want to wait at all, then pass in -1.
     *
     * @param waitMillis The number of milli-seconds to wait for a resource.
     * @return Object the resource acquired from the pool.
     * @throws NoFreeResourcesException Thrown when no free resources are
     *                                  available.
     */
    public synchronized Object acquireResource(long waitMillis)
            throws NoFreeResourcesException {

        // Caller does not want to wait for an available resource.

        if (waitMillis < 0) {
            return getResource();
        }

        // Caller is willing to wait a specified time for an available 
        // resource. If 'waitMillis' is zero, we will wait indefinately, 
        // until a resource is available.

        long timeLeft = waitMillis;
        long timeDecrement = 100;
        if (waitMillis > 0) {
            timeDecrement = timeLeft / 100;
        }
        while (true) {
            try {

                // see if a resource is ripe for the picking

                return getResource();
            } catch (NoFreeResourcesException nfre) {

                // if we have waited all we can then get out

                if ((waitMillis > 0) && (timeLeft <= 0)) {
                    throw nfre;
                }

                // wait some more

                try {
                    wait(timeDecrement);
                } catch (InterruptedException ie) {
                }
                if (waitMillis > 0) {
                    timeLeft -= timeDecrement;
                }
            }
        }
    }

    /**
     * Releases the resource back into the "free" list so it can be used again.
     *
     * @param resource The resource to release.
     */
    public void releaseResource(Object resource) {
        releaseResource(resource, false);
    }

    /**
     * Releases the resource back into the "free" list so it can be used
     * again.
     *
     * @param resource The resource to release.
     * @param isBad    Flag to determine if the resource is "bad" or not.
     */
    public void releaseResource(Object resource, boolean isBad) {

        // <KBR CONTINUUM FIX>
        if (resource == null) {
            return;
        }

        // Otherwise - we add a null resource!
        // If the resource is bad, attempt to destroy it
        if (isBad) {
            try {
                destroy(freeResources);
                rp.destroy(resource);
            } catch (ResourceProviderException rpe) {
            }
        }

        // call the synchronized method to add it back to the list and
        // decrement the number of resources checked out
        usedToFreeResources(resource, isBad);
    }

    /**
     * Determines if the resource passed in belongs to the pool (e.g., was created by
     * this pool.)
     *
     * @param resource The resource to check.
     * @return True if the resource belongs to this pool.
     */
    public boolean contains(Object resource) {
        return (usedResources.contains(resource)
                || freeResources.contains(resource));
    }

    /**
     * Destroys all resources in both the free pool and the used pool.
     */
    public synchronized void destroy() {

        // Destroy both used and free pools
        destroy(freeResources);
        destroy(usedResources);
    }

    /**
     * Iterates over apool of reosurces and delegates the
     * individual destroy to the ResourceProvider.
     *
     * @param resources either the free pool or the unused pool
     */
    private void destroy(Vector resources) {

        Enumeration e = resources.elements();

        while (e.hasMoreElements()) {
            try {

                // Defer to the resource provider for destruction of resource.
                rp.destroy(e.nextElement());
            } catch (ResourceProviderException rpe) {
            }
        }

        resources.removeAllElements();
    }

    /**
     * Method to validate all the free resources in the pool.  Validation
     * determines if the resources are still active.
     */
    public void validate() throws PoolNotAvailableException {
        if (isPoolLocked()) {
            throw new PoolNotAvailableException(lockReason);
        }

        /*
         * Go through each free resource in the pool and validate it
         */
        Object resource = null;
        for (int ii = 0; ii < freeResources.size(); ii++) {
            resource = freeResources.elementAt(ii);
            if (!rp.isValid(resource)) {
                try {
                    rp.destroy(resource);
                } catch (ResourceProviderException rpe) {
                    // ignore
                }
                freeResources.removeElementAt(ii);
            }
        }
    }

    /**
     * Returns the current number of free resources in the pool
     *
     * @return Number of free resources in the pool.
     */
    public int getFreeCount() {
        return freeResources.size();
    }

    /**
     * Gets the name of the owner of the pool.
     *
     * @return The owner of the pool.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns a resource from the pool for a client to use
     *
     * @return The resource
     * @throws NoFreeResourcesException When there are no free resources available to return.
     */
    private Object getResource()
            throws NoFreeResourcesException {

        if (isPoolLocked()) {
            throw new PoolNotAvailableException(lockReason);
        }

        Object resource = null;

        /*
         * See if there are any free resources in the pool.
         * If so return one of the resources
         */
        if (freeResources.size() > 0) {
            String err = "";

            while ((resource == null) && (freeResources.size() > 0)) {
                resource = freeResources.firstElement();

                freeResources.removeElementAt(0);

                if (resourceValidation) {
                    if (!rp.isValid(resource)) {
                        try {
                            rp.destroy(resource);
                        } catch (ResourceProviderException rpe) {
                            err = "Resource is no longer valid.  Reason: "
                                    + rpe.getMessage();
                        }
                        resource = null;
                    }
                }
            }

            if (null == resource) {
                throw new NoFreeResourcesException(err);
            }
        } else if ((usedResources.size() < max) || (max == 0)) {

            /*
             * If no free resources and we are under the limit of maximum
             * resources (or if there is no max), try to create a new resource
             */
            try {
                resource = newResource();
            } catch (ResourceProviderException rpe) {

                // Couldn't create the resource
                throw new NoFreeResourcesException(
                        "Couldn't expand the free pool size: " +
                                rpe.getMessage());
            }
        } else {
            String message =
                    "PROGRAMMING ERROR: Max resources reached!";

            // Since this method is a utility method used by the
            // acquireResource() method, we should never get here!!!
            // twp:  I disagree, it looks to me that this code
            //       would be reached everytime this method was called
            //       and all the resources in the pool were allocated
            //       (freeResources.size() ==0, max > 0, usedResources.size() == max)
            throw new NoFreeResourcesException(message);
        }

        // Successfully obtained a resource, add it to the used pool
        // before returning it to the client.
        usedResources.addElement(resource);

        return resource;
    }

    /**
     * Returns the current number of used resources in the pool
     *
     * @return Number of used resources in the pool.
     */
    public int getUsedCount() {
        return usedResources.size();
    }

    /**
     * Initializes the pool
     */
    private void initPool() {

        // Create "min" number of resources
        for (int ii = 0; ii < min; ii++) {
            try {

                // Create the resource and add it to the free list
                Object resource = newResource();

                freeResources.addElement(resource);
            } catch (ResourceProviderException rpe) {
            }
        }
    }

    /**
     * Is the pool full?
     *
     * @return Whether the pool is full
     */
    private boolean maxedOut() {

        boolean maxedOut = true;

        if (!freeResources.isEmpty() ||
                (usedResources.size() < max) || (max == 0)) {
            maxedOut = false;
        }

        return maxedOut;
    }

    /**
     * Creates a new resource.  This method will delegate to this
     * resource pool's ResourceProvider.
     *
     * @return A new resource
     * @throws ResourceProviderException Thrown when a new and valid
     *                                   resource cannot be constructed.
     */
    private Object newResource()
            throws ResourceProviderException {

        // Proxy to the resource provider for creation
        Object resource = rp.create(props);

        if (!rp.isValid(resource)) {
            rp.destroy(resource);

            throw new ResourceProviderException("Resource is invalid");
        }

        return resource;
    }

    /**
     * Moves the resource from the "used" list to the "free" list
     *
     * @param resource The resource to release
     * @param isBad    Flag to determine if the resource is "bad" or not
     */
    private synchronized void usedToFreeResources(Object resource,
                                                  boolean isBad) {

        // Only add the resource back to the list if it is good
        if (!isBad) {

            // Don't let them free the same resource twice
            if (!freeResources.contains(resource)) {
                freeResources.addElement(resource);
            }
        }

        // Good or bad, delete it from the used list
        usedResources.removeElement(resource);
        notifyAll();
    }

    /**
     * Returns whether any connections
     * in this pool are currently in use.
     *
     * @return whether any connections
     *         in this pool are currently in use.
     */
    public synchronized boolean isPoolInUse() {
        // if the size of the used pool is greater than 0, yeah - the
        // pool is in use.
        return getUsedCount() > 0;
    }

    /**
     * Returns whether any connections
     * in this pool are currently in use.
     *
     * @param timeoutSeconds wait in seconds for making a
     *                       determination
     * @return whether any connections
     *         in this pool are currently in use.
     */
    public synchronized boolean isPoolInUse(int timeoutSeconds) {
        // if the size of the used pool is greater than 0, yeah - the
        // pool is in use.

        long waitMillis;

        if (timeoutSeconds > 0) {
            waitMillis = timeoutSeconds * 1000;
        } else {
            return isPoolInUse();
        }

        // Caller is willing to wait a specified time for an available 
        // resource. If 'waitMillis' is zero, we will wait indefinately, 
        // until a resource is available.
        long timeLeft = waitMillis;
        long startTime = System.currentTimeMillis();
        while (isPoolInUse() == true && timeLeft > 0) {
            // wait
            try {
                wait(timeLeft);
            } catch (InterruptedException ie) {
                // we're being awokened by a resource becoming free
                // or by timeLeft expiring
            }
            timeLeft = waitMillis - (System.currentTimeMillis() - startTime);
        }

        return isPoolInUse();
    }

    /**
     * Locks the pool. If false is returned, follow-up with
     * isPoolInUse() calls until isPoolInUse() returns false.
     *
     * @param why            Reason for locking the pool. Unused if pool is
     *                       already locked. Reason shows up in exception messages.
     * @param timeoutSeconds wait in seconds to lock the pool and
     *                       ensure that no DirContext connections in the pool are in use.
     * @return Whether there are any pool connections in use.
     */
    public synchronized boolean lockPool(String why, int timeoutSeconds) {
        if (!isPoolLocked()) {
            // set lock flag and reason if not already locked
            locked = true; // set flag so noone can aquire a free resource
            lockReason = why;
        }

        return (isPoolInUse(timeoutSeconds));
    }

    /**
     * Unlocks the pool - making the pool available for use.
     */
    public synchronized void unlockPool() {
        if (isPoolLocked()) {
            locked = false;
            lockReason = null;
        }
    }

    /**
     * Returns whether the pool has been locked.
     *
     * @return whether the pool has been locked.
     */
    public synchronized boolean isPoolLocked() {
        return locked;
    }

    /**
     * Convenience routine for checking if the pool is unlocked
     *
     * @param timeoutSeconds wait in seconds for making a determination
     */
    public synchronized boolean isPoolLocked(int timeoutSeconds) {

        // no waiting requested
        if (timeoutSeconds <= 0) {
            return (isPoolLocked());
        }

        // Caller is willing to wait a specified time for an available resource.
        long waitMillis = timeoutSeconds * 1000;
        long timeLeft = waitMillis;
        long startTime = System.currentTimeMillis();
        while ((!isPoolLocked()) && (timeLeft > 0)) {
            try {
                wait(timeLeft);
            } catch (InterruptedException ie) {
                // we're being awokened by a resource becoming free
                // or by timeLeft expiring
            }
            timeLeft = waitMillis - (System.currentTimeMillis() - startTime);
        }

        return (isPoolLocked());
    }

    /**
     * Convenience routine for checking if the pool is unlocked and
     * has no connections in use.
     */
    public synchronized boolean isPoolLockedAndUnused() {
        return isPoolLockedAndUnused(-1);
    }

    /**
     * Convenience routine for checking if the pool is unlocked and
     * has no connections in use.
     *
     * @param timeoutSeconds wait in seconds for making a
     *                       determination
     */
    public synchronized boolean isPoolLockedAndUnused(int timeoutSeconds) {
        return !isPoolInUse(timeoutSeconds) && isPoolLocked();
    }
}
