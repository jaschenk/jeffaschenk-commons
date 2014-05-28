package jeffaschenk.commons.identifiers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
    Test RandomGUID Generation
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class TestRandomGUID extends AbstractJUnit4SpringContextTests {

    // ***************************************
    // Logging
    protected static Log log = LogFactory.getLog(TestRandomGUID.class);

     @Test
    public void testOne() throws Exception {
        assertNotNull("RandomGUID Returned Null, very Bad!", new RandomGUID().toString());
    }


}
