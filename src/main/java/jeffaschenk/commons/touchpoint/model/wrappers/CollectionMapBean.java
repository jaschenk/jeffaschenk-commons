package jeffaschenk.commons.touchpoint.model.wrappers;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Wrapper Class
 *
 * @author jeffaschenk@gmail.com
 */
public class CollectionMapBean {

    private Map<String, Object> map = new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public CollectionMapBean(Map<String, Object> map) {
        this.map = map;
    }

    public CollectionMapBean() {
    }
}
