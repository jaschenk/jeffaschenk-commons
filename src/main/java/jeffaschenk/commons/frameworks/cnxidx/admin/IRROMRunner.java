package jeffaschenk.commons.frameworks.cnxidx.admin;


import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Java class for providing either simple HTML formatting of a
 * java Executed program.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class IRROMRunner {

    private static String MP = "IRROMRunner: ";

    private static String[] JRUN = {"java",
            "-Xms320000000",
            "-Xmx640000000"};

    private String[] jExec;

    private String Jname = "OMRunner: ";

    private boolean DEBUG = false;

    /**
     * IRROMRunner contructor.
     *
     * @param _tagname for Prefix of Output.
     */
    IRROMRunner(String _tagname) {
        Jname = _tagname;
    } // End of Constructor.

    /**
     * set DEBUGGING Method.
     *
     * @param _indicator for Debugging Indicator.
     */
    public void setDEBUG(boolean _indicator) {
        DEBUG = _indicator;
    } // End of Method.

    /**
     * setFunction method, to set up runtime JAVA program name and
     * arguments.
     *
     * @param _jAL for Java Command Arguments.
     */
    public void setFUNCTION(List<String> _jAL) {

        // *********************************
        // First Create a List of Properties
        // that need to be included for the
        // Run.  Anything I have let this
        // RUN inherit.
        ArrayList<String> _prop = new ArrayList<>();
        Properties _myprop = System.getProperties();
        Set _myprops = _myprop.keySet();
        Iterator itr = _myprops.iterator();
        while (itr.hasNext()) {
            String _pname = (String) itr.next();
            _prop.add("-D" + _pname + "=" +
                    _myprop.getProperty(_pname));
        } // End of While Loop.

        // *******************************************
        // Size up the String Length.
        jExec = new String[(JRUN.length + _jAL.size() + _prop.size())];

        // *********************************
        // Set up our Command with Options.
        System.arraycopy(JRUN, 0, jExec, 0, JRUN.length);

        // ***************************************
        // Now Set the Runtime Properties.
        int SE = JRUN.length;
        for (int i = 0; i < _prop.size(); i++) {
            jExec[SE] = (String) _prop.get(i);
            SE++;
        } // End of Foor Loop.

        // ***************************************
        // Now Set the Runtime class and arguments
        for (int i = 0; i < _jAL.size(); i++) {
            jExec[SE] = (String) _jAL.get(i);
            SE++;
        } // End of Foor Loop.

        // ***************************************
        // Show Array.
        if (DEBUG) {
            for (int i = 0; i < jExec.length; i++) {
                System.out.println("jExec[" + i +
                        "] [" + jExec[i] + "]");
            } // End of Foor Loop.
        } // End of DEBUG

    } // End of setFUNCTION Method.

    /**
     * Perform a local run of a Java Program with the specified Arguments.
     */
    public void performFUNCTION() {

        // ****************************************
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();

        // ************************************
        // Perform Function.
        try {
            Process p = Runtime.getRuntime().exec(jExec);

            BufferedReader se = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            BufferedReader so = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            // **************************
            // Show the Output.
            String buf;
            System.out.println(Jname + ": Output Messages:");
            while ((buf = so.readLine()) != null) {
                System.out.println(Jname + ": " + buf);
            }

            // **************************
            // Show the Error Messages.
            System.out.println(Jname + ": Error Messages:");
            while ((buf = se.readLine()) != null) {
                System.out.println(Jname + ": " + buf);
            }

            // **************************
            // Wait for Completion.
            int RC = p.waitFor();

            // ****************************************
            // Note The End Time.
            elt.setEnd();

            // ***************************************
            // Show the Counts and Elapsed Time.
            System.out.println(Jname + ": Final Results");
            System.out.println(Jname + ": Task Return Code:" + RC);
            System.out.println(Jname + ": Elapsed Time to perform Function " + elt.getElapsed());

        } // End of exception
        catch (Exception e) {
            System.out.println(Jname + ": IRR Status Exception: " + e);
            e.printStackTrace();
        } // End of exception

    } ///:~ End of perform Method.

} ///:~ End of IRROMRunner Class
