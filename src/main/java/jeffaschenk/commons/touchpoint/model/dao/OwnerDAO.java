package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.touchpoint.model.Owner;

public interface OwnerDAO extends EntityDAO {

    public Owner findOwnerByById(Integer id, Object... optionalParameters);

}
