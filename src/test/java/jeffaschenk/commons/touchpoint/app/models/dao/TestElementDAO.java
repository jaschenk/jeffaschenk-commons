package jeffaschenk.commons.touchpoint.app.models.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import jeffaschenk.commons.touchpoint.model.Element;
import jeffaschenk.commons.touchpoint.model.dao.ElementDAO;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class TestElementDAO extends AbstractJUnit4SpringContextTests {

    // ***************************************
    // Logging
    protected static Log log = LogFactory.getLog(TestElementDAO.class);

    @Autowired
    private ElementDAO elementDAO;


    @Test
    public void testCreate() throws Exception {
        assertNotNull("Owner DAO not Autowired, very Bad!", elementDAO);
        Element element = new Element();
        element.setName("Test:" + System.currentTimeMillis());

        Integer newId = elementDAO.createEntity(element);
        assertNotNull(newId);

        element = elementDAO.findElementByById(newId);
        assertNotNull(element);
    }

}
