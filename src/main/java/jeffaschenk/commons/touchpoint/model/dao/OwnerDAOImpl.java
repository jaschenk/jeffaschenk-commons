package jeffaschenk.commons.touchpoint.model.dao;

/**
 * Owner Entity Service DAO Implementation.
 *
 * @author JeffASchenk@gmail.com
 * @version $Id: $
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import jeffaschenk.commons.touchpoint.model.Owner;

@Repository("owner")
public class OwnerDAOImpl extends EntityDAOImpl implements OwnerDAO {

    /**
     * Logging
     */
    private static Log log = LogFactory.getLog(OwnerDAOImpl.class);

    @Override
    public Owner findOwnerByById(Integer id, Object... optionalParameters) {
        return super.readDistinctEntity(Owner.class, id, optionalParameters);
    }
}
