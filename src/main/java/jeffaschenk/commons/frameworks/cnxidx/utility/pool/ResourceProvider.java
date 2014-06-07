package jeffaschenk.commons.frameworks.cnxidx.utility.pool;


import jeffaschenk.commons.exceptions.ResourceProviderException;

import java.util.Properties;

/**
 * This interface defines a resource provider, which manages lifecycle
 * aspects of scarce resources.  A resource for our purposes is a scarce
 * resource which is managed by a Resource Pool.  All resources which are
 * managed by a Resource Pool must implement this interface. Thus, through
 * this interface, resources are created, destroyed, and checked for
 * validity. Resources managed by ResourcePool must implement this interface.
 */
public interface ResourceProvider {

    /**
     * Creates the resource.  Returns the resource as a java.lang.Object
     * <p/>
     * <b>Note:</b> Create should not call <pre>isValid</pre> since that is
     * the responsibility of the ResourcePool.
     *
     * @param props Properties which are used to create the resource.
     * @return An Object (i.e., a scarce resource to be placed into the pool.
     * @throws jeffaschenk.commons.exceptions.ResourceProviderException Thrown when the provider is unable to create a new resource.
     */
    public Object create(Properties props)
            throws ResourceProviderException;

    /**
     * Shuts down or closes the resource.
     *
     * @param resource The resource to shut down
     * @throws ResourceProviderException Thrown when the resource handed to the provider is invalid (e.g., the wrong
     *                                   resource type.)  Not thrown if the resource is unable to be
     *                                   closed/destroyed properly.
     */
    public void destroy(Object resource)
            throws ResourceProviderException;

    /**
     * Determines if the resource is in a valid state and can be safely used. Validation
     * varies between different resource types.
     *
     * @param resource The resource to validate.
     * @return True if the resource is in a valid state. Otherwise, false.
     */
    public boolean isValid(Object resource);
}
