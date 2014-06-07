package jeffaschenk.commons.touchpoint.model.threads;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * CircularObjectStackTest.java
 * Created on September 30, 2005, 9:44 AM
 * Test CircularObjectStack.
 *
 * @author jeff.schenk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext.xml")
public class CircularObjectStackTest extends AbstractJUnit4SpringContextTests {

    // *******************************
    // Test Static Constants.
    private static final String[] sarray = {"Zero",
            "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight",
            "Nine", "Ten"};

    @Test
    public void testCircularObjectStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

    } // End of testCircularObjectStack

    @Test
    public void testInsertingOneEntryInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Inserting One Object into Stack...");
        cos.insert(this.sarray[1]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have one Object in Stack!", 1, cos.size());
        System.out.println("** Starting getNext() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.getNext();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[1], x);
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 1, looped);
    } // End of testOneEntryInStack

    @Test
    public void testPushingOneEntryInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Pushing One Object into Stack...");
        cos.insert(this.sarray[1]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have one Object in Stack!", 1, cos.size());
        System.out.println("** Starting pop() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.pop();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[1], x);
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 1, looped);
    } // End of testPushingOneEntryInStack

    @Test
    public void testInsertingTwoEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Inserting Two Objects into Stack...");
        cos.insert(this.sarray[2]);
        cos.insert(this.sarray[1]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have Two Objects in Stack!", 2, cos.size());
        System.out.println("** Starting getNext() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.getNext();
            System.out.println(x.toString());
            if (looped == 1) {
                assertEquals("Object Obtained is not what was expected!", this.sarray[1], x);
            } else if (looped == 2) {
                assertEquals("Object Obtained is not what was expected!", this.sarray[2], x);
            }
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 2, looped);
    } // End of testInsertingTwoEntriesInStack

    @Test
    public void testPushingTwoEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Pushing Two Objects into Stack...");
        cos.push(this.sarray[1]);
        cos.push(this.sarray[2]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have Two Objects in Stack!", 2, cos.size());
        System.out.println("** Starting pop() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.pop();
            System.out.println(x.toString());
            if (looped == 1) {
                assertEquals("Object Obtained is not what was expected!", this.sarray[2], x);
            } else if (looped == 2) {
                assertEquals("Object Obtained is not what was expected!", this.sarray[1], x);
            }
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 2, looped);
    } // End of testPushingTwoEntriesInStack

    @Test
    public void testInsertingThreeEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Inserting Three Objects into Stack...");
        cos.insert(this.sarray[3]);
        cos.insert(this.sarray[2]);
        cos.insert(this.sarray[1]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have Three Objects in Stack!", 3, cos.size());
        System.out.println("** Starting getNext() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.getNext();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[looped], x);
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 3, looped);
    } // End of testInsertingThreeEntriesInStack

    @Test
    public void testPushingThreeEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Pushing Three Objects into Stack...");
        cos.push(this.sarray[3]);
        cos.push(this.sarray[2]);
        cos.push(this.sarray[1]);
        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, should have Three Objects in Stack!", 3, cos.size());
        System.out.println("** Starting pop() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            looped++;
            Object x = cos.pop();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[looped], x);
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 3, looped);
    } // End of testPushingThreeEntriesInStack

    @Test
    public void testInsertingMultipleEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Inserting Multiple Objects into Stack...");
        for (int i = (sarray.length - 1); 0 <= i; i--) {
            cos.insert(this.sarray[i]);
        }

        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, Differing Stack Size!", sarray.length, cos.size());
        System.out.println("** Starting getNext() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            Object x = cos.getNext();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[looped], x);
            looped++;
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", sarray.length, looped);
    } // End of testInsertingMultipleEntriesInStack


    @Test
    public void testPushingMultipleEntriesInStack() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // *************************************
        // Fill the Stack.
        System.out.println("");
        System.out.println("** Pushing Multiple Objects into Stack...");
        for (int i = 0; i < sarray.length; i++) {
            cos.push(this.sarray[i]);
        }

        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, Differing Stack Size!", sarray.length, cos.size());
        System.out.println("** Starting pop() Loop...");
        int looped = sarray.length - 1;
        while (cos.hasMoreNodes()) {
            Object x = cos.pop();
            System.out.println(x.toString());
            assertEquals("Object Obtained is not what was expected!", this.sarray[looped], x);
            looped--;
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", -1, looped);
    } // End of testInsertingMultipleEntriesInStack

    @Test
    public void testVariousAccess() {

        // ***********************************
        // Instantiate the Exercizer Object.
        CircularObjectStack cos = new CircularObjectStack();

        // ***********************************
        // Verify we have an Object.
        assertNotNull("CircularObjectStack Object is NULL, very Bad!", cos);

        // *************************************
        // Verify we have zero Objects in Stack
        assertEquals("Stack is Corrupted, should have zero Objects in Stack!", 0, cos.size());

        // ****************************************
        // Test Multiple push and inserted values.
        System.out.println("");
        System.out.println("** Test Multiple push and inserted values.");
        cos.insert(sarray[5]);
        cos.push(sarray[4]);
        cos.push(sarray[3]);
        cos.push(sarray[2]);
        cos.push(sarray[1]);
        cos.insert(sarray[0]);
        cos.insert(sarray[6]);

        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, Differing Stack Size!", 7, cos.size());
        System.out.println("** Starting getNext() Loop...");
        int looped = 0;
        while (cos.hasMoreNodes()) {
            Object x = cos.getNext();
            System.out.println(x.toString());
            looped++;
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 7, looped);

        // ************************************************
        // Test simple one value pushed the other inserted.
        System.out.println("");
        System.out.println("** Test simple one value pushed the other inserted.");
        cos.push(new String("J1"));
        cos.insert(new String("J2"));

        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, Differing Stack Size!", 2, cos.size());
        System.out.println("** Starting getNext() Loop...");
        looped = 0;
        while (cos.hasMoreNodes()) {
            Object x = cos.getNext();
            System.out.println(x.toString());
            looped++;
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 2, looped);

        // **********************************************
        // Test Multiple push and single inserted values.
        System.out.println("");
        System.out.println("** Test Multiple push and single inserted values.");
        cos.push(new String("X1"));
        cos.push(new String("X2"));
        cos.insert(new String("X0"));

        System.out.println("** Number of Objects in Stack:" + cos.size());
        System.out.println("**         Stack:" + cos.showStack());
        System.out.println("** Reverse Stack:" + cos.showReverseStack());
        assertEquals("Stack is Corrupted, Differing Stack Size!", 3, cos.size());
        System.out.println("** Starting pop() Loop...");
        looped = 0;
        while (cos.hasMoreNodes()) {
            Object x = cos.pop();
            System.out.println(x.toString());
            looped++;
        } // End of While Loop.
        assertEquals("Did not Loop the number of Expected Times!", 3, looped);

        // **********************************
        // Finalliy there should be nothing
        // in the stack.
        assertEquals("Stack is Corrupted, Differing Stack Size!", 0, cos.size());
    } // End of testVariousAccess

} ///:~ End of CircularObjectStackTest Class.
