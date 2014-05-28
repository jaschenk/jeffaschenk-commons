package jeffaschenk.commons.model.oxm.registereduserprofile.mapper;

import jeffaschenk.commons.model.oxm.registereduserprofile.RegisteredUserProfileMeta;

/**
 * RegisteredUserProfileMetaMapper
 * JAVA Meta Object to Mapper Serialization.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 */

public interface RegisteredUserProfileMetaMapper {

    String toXml(RegisteredUserProfileMeta registeredUserProfileMeta);

    RegisteredUserProfileMeta toRegisteredUserProfileMeta(String xmldata);

}
