package jeffaschenk.commons.model.oxm.searchresultsgroup.mapper;

import jeffaschenk.commons.model.oxm.searchresultsgroup.SearchResultGroupMeta;
import jeffaschenk.commons.model.oxm.searchresultsgroup.SearchResultGroupMetaSpringBeanReference;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * RegisteredUserProfileMetaMapper Implementation
 *
 * JAVA Object to Mapper Serialization.
 *
 * @author jeffaschenk@gmail.com
 *         <p/>
 */
@Service("metaMapper")
public class MetaMapperImpl implements MetaMapper {

    private static final Log log = LogFactory.getLog(MetaMapperImpl.class);

    @Override
    public String toXml(SearchResultGroupMeta searchResultGroupMeta) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchResultGroupMeta.class, SearchResultGroupMetaSpringBeanReference.class);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Writer sw = new StringWriter();
            marshaller.marshal(searchResultGroupMeta, sw);
            return sw.toString();
          } catch (JAXBException jaxbException) {
            log.error("Marshaling Exception for Search Result Group Meta:["+jaxbException.getMessage()+"]",jaxbException);
            return null;
        }
    }

    @Override
    public SearchResultGroupMeta toSearchResultGroupMeta(String xmlData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchResultGroupMeta.class, SearchResultGroupMetaSpringBeanReference.class);

            javax.xml.bind.Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (SearchResultGroupMeta) unmarshaller.unmarshal(new StringReader(xmlData));
        } catch (JAXBException jaxbException) {
            log.error("Un-marshaling Exception for Search Result Group Meta:["+jaxbException.getMessage()+"]",jaxbException);
            return null;
        }
    }



}
