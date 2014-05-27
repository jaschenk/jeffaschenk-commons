package jeffaschenk.commons.touchpoint.model.dao;

import jeffaschenk.commons.touchpoint.model.Element;

public interface ElementDAO extends EntityDAO {

    public Element findElementByById(Integer id, Object... optionalParameters);

}
