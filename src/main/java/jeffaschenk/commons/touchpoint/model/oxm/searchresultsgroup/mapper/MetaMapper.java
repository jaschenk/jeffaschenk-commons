package jeffaschenk.commons.touchpoint.model.oxm.searchresultsgroup.mapper;

import jeffaschenk.commons.touchpoint.model.oxm.searchresultsgroup.SearchResultGroupMeta;

/**
 * RegisteredUserProfileMetaMapper
 * JAVA Meta Object to Mapper Serialization.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 */

public interface MetaMapper {

    String toXml(SearchResultGroupMeta searchResultGroupMeta);

    SearchResultGroupMeta toSearchResultGroupMeta(String xmldata);

}
