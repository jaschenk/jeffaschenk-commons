package jeffaschenk.commons.system.internal.cms;

/**
 *
 * Content Management Service Interface
 * Provides Access to resolve any dynamic content directly from an established cache or direct from CMS.
 *
 *
 * @author jeffaschenk@gmail.com
 *
 */
public interface ContentManagementService {

    String grabDynamicContent(String path);

    String grabLatestFeedContent(String path);

    String grabToday();

}
