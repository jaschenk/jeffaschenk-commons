package jeffaschenk.commons.touchpoint.model.dao;

/**
 * Element Entity Service DAO Implementation.
 *
 * @author JeffASchenk@gmail.com
 * @version $Id: $
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import jeffaschenk.commons.touchpoint.model.Element;

@Repository("element")
public class ElementDAOImpl extends EntityDAOImpl implements ElementDAO {

    /**
     * Logging
     */
    private static Log log = LogFactory.getLog(ElementDAOImpl.class);

    @Override
    public Element findElementByById(Integer id, Object... optionalParameters) {
        return super.readDistinctEntity(Element.class, id, optionalParameters);
    }
}
