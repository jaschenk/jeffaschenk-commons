package jeffaschenk.commons.frameworks.cnxidx.simulation;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;

/**
 * Java Class to provide a Entry class for DIT Simulation Populations.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2003
 */

public class idxSimulatorProfileEntry {

    public String SimulatorProfileName = "";
    public int InitialConfigurationSize = 0;
    public int IncrementalGrowthOfConfiguration = 0;
    public int DailyFrequencyOfConfigurationChanges = 0;
    public int InitialNumberOfResources = 0;
    public int IncrementalGrowthOfResources = 0;

    // **************************************
    // Indicators for this Profile.
    public boolean CompressConfigurations = false;
    public boolean PersistSearchIdentities = false;

    // **************************************
    // Lap Times Per this Profile.
    public idxLapTime CFG_WRITES = new idxLapTime();
    public idxLapTime CFG_READS = new idxLapTime();

    /**
     * Initial Constructor used when no argument supplied.
     */
    public idxSimulatorProfileEntry() {
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
        return (" Profile Name: " + SimulatorProfileName + ls +
                "\t           Initial Cfg Size: " + InitialConfigurationSize + " Bytes" + ls +
                "\t                Grow Cfg By: " + IncrementalGrowthOfConfiguration + " Bytes" + ls +
                "\t        Daily Cfg Frequency: " + DailyFrequencyOfConfigurationChanges + ls +
                "\tInitial Number of Resources: " + InitialNumberOfResources + ls +
                "\t          Grow Resources By: " + IncrementalGrowthOfResources +
                ls);
    } // End of Method.

} ///: End of idxSimulatorProfileEntry Class.
