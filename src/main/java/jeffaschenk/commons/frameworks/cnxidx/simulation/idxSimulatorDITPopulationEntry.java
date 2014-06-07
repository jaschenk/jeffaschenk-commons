package jeffaschenk.commons.frameworks.cnxidx.simulation;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;

/**
 * Java Class to provide a Entry class for DIT Simulation Populations.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2003
 */

public class idxSimulatorDITPopulationEntry {

    public String X500DN;
    public String LDAPDN;
    public String SimulationProfileName;

    // **************************************
    // Lap Times Per this Container.
    public idxLapTime CFG_WRITES = new idxLapTime();
    public idxLapTime CFG_READS = new idxLapTime();

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxSimulatorDITPopulationEntry() {
    } // end of Constructor

    /**
     * toString Object Converter.
     */
    public String toString() {
        String ls = "\n";
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {
            ls = "\r\n";
        }
        return ("Container: " + LDAPDN + ls +
                "\tSimulation Profile Name: " + SimulationProfileName +
                ls);
    } // End of Method.

} ///: End of idxSimulatorDITPopulationEntry Class.
