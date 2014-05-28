package jeffaschenk.commons.touchpoint.app.models.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import jeffaschenk.commons.touchpoint.model.Owner;
import jeffaschenk.commons.touchpoint.model.dao.OwnerDAO;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class TestOwnerDAO extends AbstractJUnit4SpringContextTests {

    // ***************************************
    // Logging
    protected static Log log = LogFactory.getLog(TestOwnerDAO.class);

    @Autowired
    private OwnerDAO ownerDAO;


    @Test
    public void testCreate() throws Exception {
        assertNotNull("Owner DAO not Autowired, very Bad!", ownerDAO);
        Owner owner = new Owner();
        owner.setName("Test:" + System.currentTimeMillis());

        Integer newId = ownerDAO.createEntity(owner);
        assertNotNull(newId);

        owner = ownerDAO.findOwnerByById(newId);
        assertNotNull(owner);
    }

}
