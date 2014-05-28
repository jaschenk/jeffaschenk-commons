package jeffaschenk.commons.model.oxm.registereduserprofile.mapper;

import jeffaschenk.commons.model.oxm.registereduserprofile.RegisteredUserProfileMeta;
import jeffaschenk.commons.model.wrappers.DetectedClientTimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
@Service("registeredUserProfileMetaMapper")
public class RegisteredUserProfileMetaMapperImpl implements RegisteredUserProfileMetaMapper {

    private static final Log log = LogFactory.getLog(RegisteredUserProfileMetaMapperImpl.class);

    @Override
    public String toXml(RegisteredUserProfileMeta registeredUserProfileMeta) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RegisteredUserProfileMeta.class, DetectedClientTimeZone.class);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Writer sw = new StringWriter();
            marshaller.marshal(registeredUserProfileMeta, sw);
            return sw.toString();
          } catch (JAXBException jaxbException) {
            log.error("Marshaling Exception for Search Result Group Meta:["+jaxbException.getMessage()+"]",jaxbException);
            return null;
        }
    }

    @Override
    public RegisteredUserProfileMeta toRegisteredUserProfileMeta(String xmlData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RegisteredUserProfileMeta.class, DetectedClientTimeZone.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return (RegisteredUserProfileMeta) unmarshaller.unmarshal(new StringReader(xmlData));
        } catch (JAXBException jaxbException) {
            log.error("Un-marshaling Exception for Search Result Group Meta:["+jaxbException.getMessage()+"]",jaxbException);
            return null;
        }
    }

}
