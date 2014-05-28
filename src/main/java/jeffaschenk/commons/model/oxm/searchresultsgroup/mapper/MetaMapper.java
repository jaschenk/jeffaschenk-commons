package jeffaschenk.commons.model.oxm.searchresultsgroup.mapper;

import jeffaschenk.commons.model.oxm.searchresultsgroup.SearchResultGroupMeta;

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
