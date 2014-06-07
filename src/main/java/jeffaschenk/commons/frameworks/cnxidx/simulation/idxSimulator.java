package jeffaschenk.commons.frameworks.cnxidx.simulation;

import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxElapsedTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxLapTime;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxParseDN;
import jeffaschenk.commons.frameworks.cnxidx.utility.ldap.idxTimeStamp;

import java.util.*;
import java.io.*;

/**
 * Java class to perform Timing and Performance Simulation for
 * writing various FRAMEWORK Base objects, such as Resources and their
 * associated configurations. This will allow for FRAMEWORK to obtain
 * a sampling of a customers Network Resource Configurations and then
 * use those samples to create a simulation test of the customer's environment
 * to show expected performance numbers.
 *
 * @author jeff.schenk
 * @version 3.0 $Revision
 * Developed 2003
 */

public class idxSimulator {

    protected boolean VERBOSE = false;

    protected String LINE_SEP = "\n";

    protected Properties SimulatorConfig = new Properties();
    protected Properties PoolConfig = new Properties();

    protected idxTimeStamp TIMESTAMP = null;
    protected idxRandomDataGenerator RDG = null;

    protected int MAX_XML_SIZE = ((1024 * 16) * 1024); // 16MB Maximum XML Document Size.
    protected int MAX_CLI_SIZE = ((1024 * 8) * 1024);  // 8MB Maximum CLI Size.

    // ***********************************************
    // Filenames to Watch out for.
    protected String STOP_FILE_TO_WATCH = "";            // File to Indicate a Stop.
    protected String PAUSE_FILE_TO_WATCH = "";           // File to Indicate a Pause.
    protected String RESUME_FILE_TO_WATCH = "";          // File to Indicate a Resume.
    protected boolean STOP_PROCESSING = false;           // Global Stop Indicator.

    // ***********************************************
    // Process Sequence.
    protected LinkedList<String> SimulationProcessSequence = new LinkedList<>();

    // ***********************************************
    // Search Filter List
    protected LinkedList<String> SearchFilterList = new LinkedList<>();
    protected String SEARCH_FOR_UNIQUE_IP_ADDRESS = "";

    // ***********************************************
    // Population Resources
    protected String DIT_ROOT = "";
    protected TreeMap<String,idxSimulatorDITPopulationEntry> ResourceDITPopulation = new TreeMap<>();
    protected TreeMap<String, idxSimulatorProfileEntry> ResourceSizeProfiles = new TreeMap<>();

    // ***********************************************
    // LAP Timers
    protected idxLapTime LP_RCU_ADDS = new idxLapTime();
    protected idxLapTime LP_REUID_ADDS = new idxLapTime();
    protected idxLapTime LP_RMGMT_ADDS = new idxLapTime();
    protected idxLapTime LP_CEUID_ADDS = new idxLapTime();
    protected idxLapTime LP_UOWUID_ADDS = new idxLapTime();
    protected idxLapTime LP_AGUID_ADDS = new idxLapTime();
    protected idxLapTime LP_LRP_ADDS = new idxLapTime();
    protected idxLapTime LP_CFG_READS = new idxLapTime();

    protected idxLapTime LP_ADDS = new idxLapTime();
    protected idxLapTime LP_DELETES = new idxLapTime();
    protected idxLapTime LP_MODS = new idxLapTime();
    protected idxLapTime LP_SEARCH = new idxLapTime();
    protected idxLapTime LP_READS = new idxLapTime();

    /**
     * Initial constructor with no Parameters.
     */
    public idxSimulator() {
        TIMESTAMP = new idxTimeStamp();
        TIMESTAMP.enableLocalTime();
        RDG = new idxRandomDataGenerator();
        RDG.setVerbose(true);

        // *****************************
        // Create our Line Seperator.
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("win")) {
            LINE_SEP = "\r\n";
        }

    } // End of Constructor.

    /**
     * Method to perform initial setup facilities.
     */
    public void setup() throws Exception {

        // ****************************************    	
        // Note The Start Time.
        idxElapsedTime elt = new idxElapsedTime();
        System.out.println("**");
        System.out.println("** Simulator Beginning Setup Phase.");

        // ***************************************************
        // Now we must obtain our Pool Runtime Properties.
        //
        try {
            PoolConfig.load(new FileInputStream(System.getProperty("framework.pm.config")));
        } catch (Exception e) {
            System.err.println("Unable to obtain Pool Configuration Properties.");
            throw (new Exception("Unable to obtain Pool Configuration Properties., reason:[" +
                    e.toString() + "]"));
        } // End of Exception.

        // ***************************************************
        // Now we must obtain our simulation properties.
        //
        try {
            SimulatorConfig.load(new FileInputStream(System.getProperty("simulator.config")));
        } catch (Exception e) {
            System.err.println("Unable to obtain Simulator Configuration Properties.");
            throw (new Exception("Unable to obtain Simulator Configuration Properties., reason:[" +
                    e.toString() + "]"));
        } // End of Exception.

        // ******************************************
        // Determine VERBOSITY Level.
        VERBOSE = convertStringtoBoolean(
                SimulatorConfig.getProperty("simulator.verbose.logging", "false"));

        // ******************************************
        // Obtain Global Files Names to Trigger
        // Internal Stop, Pause or Resume.
        STOP_FILE_TO_WATCH = SimulatorConfig.getProperty("simulator.stop.filename", "");
        PAUSE_FILE_TO_WATCH = SimulatorConfig.getProperty("simulator.pause.filename", "");
        RESUME_FILE_TO_WATCH = SimulatorConfig.getProperty("simulator.resume.filename", "");

        // ************************************
        // Build the Simulation Sequence.
        String _PLIST = SimulatorConfig.getProperty("simulator.process.sequence");
        buildProcessSequence(_PLIST);

        // ************************************
        // Build the Search Filter List.
        String _SLIST = SimulatorConfig.getProperty("simulator.search.filters");
        buildSearchFilterList(_SLIST);

        // ************************************
        // Build the Word Dictionary.
        String WORD_DICTIONARY = SimulatorConfig.getProperty("simulator.word.dictionary");
        RDG.buildWordDictionary(WORD_DICTIONARY);

        // ************************************
        // Build the Common Search Word List.
        String _ALIST = SimulatorConfig.getProperty("simulator.common.search.word.list");
        RDG.buildSearchWordList(_ALIST);

        // ************************************
        // Build the Vendor List.
        _ALIST = SimulatorConfig.getProperty("simulator.resource.vendor.list");
        RDG.buildVendorList(_ALIST);

        // ************************************
        // Build the Type List.
        _ALIST = SimulatorConfig.getProperty("simulator.resource.type.list");
        RDG.buildTypeList(_ALIST);

        // ************************************
        // Build the Model List.
        _ALIST = SimulatorConfig.getProperty("simulator.resource.model.list");
        RDG.buildModelList(_ALIST);

        // ************************************
        // Build the Actual Model List.
        _ALIST = SimulatorConfig.getProperty("simulator.resource.actual.model.list");
        RDG.buildActualModelList(_ALIST);

        // ************************************
        // Build the OS Version List.
        _ALIST = SimulatorConfig.getProperty("simulator.resource.os.version.list");
        RDG.buildOSVersionList(_ALIST);

        // ****************************************
        // Obtain any Override to Max Phrases.
        _ALIST = SimulatorConfig.getProperty("simulator.max.number.phrases");
        if ((_ALIST != null) &&
                (!_ALIST.equalsIgnoreCase(""))) {
            int _num = convertHumanNumber(_ALIST);
            if (_num > -1) {
                RDG.setMaxPhraseSize(_num);
            }
        } // End of If.

        // ****************************************
        // Obtain any Override to Max Phrase Size.
        _ALIST = SimulatorConfig.getProperty("simulator.max.phrase.size");
        if ((_ALIST != null) &&
                (!_ALIST.equalsIgnoreCase(""))) {
            int _num = convertHumanNumber(_ALIST);
            if (_num > -1) {
                RDG.setMaxPhraseSize(_num);
            }
        } // End of If.

        // ****************************************
        // Obtain any Override to Max String Size. 
        _ALIST = SimulatorConfig.getProperty("simulator.max.string.size");
        if ((_ALIST != null) &&
                (!_ALIST.equalsIgnoreCase(""))) {
            int _num = convertHumanNumber(_ALIST);
            if (_num > -1) {
                RDG.setMaxStringSize(_num);
            }
        } // End of If.

        // *****************************************
        // Show Dictionary Sizes.
        RDG.showSizes();

        // *****************************************
        // OBtain a Specified Unique IP ADdress
        // for Search Tests.
        SEARCH_FOR_UNIQUE_IP_ADDRESS = SimulatorConfig.getProperty("simulator.unique.ipaddress", "1.1.1.1");

        // ****************************************
        // Obtain the DIT ROOT.
        DIT_ROOT = SimulatorConfig.getProperty("simulator.dit.root");
        if ((DIT_ROOT == null) ||
                (DIT_ROOT.equalsIgnoreCase(""))) {
            DIT_ROOT = "dc=com";
        }

        // ****************************************
        // Obtain DIT Population Properties.
        obtainDITPopulationProperties();

        // *****************************************
        // Obtain Resource Sizing Profiles.
        obtainResourceSizeProfileProperties();

        // ****************************************
        // Perform Profile Assignments to DIT
        // Containers.
        obtainProfileAssignmentProperties();

        // *****************************************
        // Show Profiles.
        System.out.println("**");
        System.out.println("** Simulator Resource Profiles:");
        Set mySet = ResourceSizeProfiles.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            idxSimulatorProfileEntry _spe =
                    (idxSimulatorProfileEntry) oit.getValue();

            System.out.println(_spe.toString());

        } // End of While Loop.

        // *****************************************
        // Show DIT Population.
        if (VERBOSE) {
            System.out.println("**");
            System.out.println("** Simulator DIT Population:");
            mySet = ResourceDITPopulation.entrySet();
            itr = mySet.iterator();
            while (itr.hasNext()) {
                Map.Entry oit = (Map.Entry) itr.next();
                idxSimulatorDITPopulationEntry _sdp =
                        (idxSimulatorDITPopulationEntry) oit.getValue();

                // ***************************
                // Only show this which
                // have a profile assigned.
                if ((_sdp.SimulationProfileName == null) ||
                        (_sdp.SimulationProfileName.equalsIgnoreCase("")) ||
                        (_sdp.SimulationProfileName.equalsIgnoreCase("none"))) {
                    continue;
                }

                System.out.println(_sdp.toString());

            } // End of While Loop.
        } // End of VERBOSE.

        // *****************************************
        // Show the Duration of the Setup.
        elt.setEnd();
        System.out.println("**");
        System.out.println("** Simulator Setup Successfully Completed within: " + elt.getElapsed());
        System.out.println("**");
    } // End of setup Method.

    /**
     * Method to build a LinkedList from a String.
     *
     * @param str String of Comma Seperated Data.
     */
    private void buildProcessSequence(String str) {

        // *******************************
        // Is this a Null or Blank Line?
        // Ignore it...
        if ((str == null) || (str.equals(""))) {
            return;
        }

        // *******************************
        // Parse the String Data.
        StringTokenizer st = new StringTokenizer(str, ",:;\n\r");
        while (st.hasMoreTokens()) {
            String LWORD = st.nextToken();
            SimulationProcessSequence.addLast(LWORD);
        } // End of Tokenizer While.

        // ******************
        // Return.
        return;

    } // End of buildProcessSequence Method.

    /**
     * Method to build a LinkedList from a String.
     *
     * @param str String of Comma Seperated Data.
     */
    private void buildSearchFilterList(String str) {

        // *******************************
        // Is this a Null or Blank Line?
        // Ignore it...
        if ((str == null) || (str.equals(""))) {
            return;
        }

        // *******************************
        // Parse the String Data.
        StringTokenizer st = new StringTokenizer(str, ",:;\n\r");
        while (st.hasMoreTokens()) {
            String LWORD = st.nextToken();
            SearchFilterList.addLast(LWORD);
        } // End of Tokenizer While.

        // ******************
        // Return.
        return;

    } // End of buildSearchFilterList Method.

    /**
     * getHumanNumber method to parse a number from a
     * human readable form.
     *
     * @param _indicator of Human Readable number.
     * @return int of the numeric value.
     */
    protected boolean convertStringtoBoolean(String _indicator) {
        if ((_indicator == null) ||
                (_indicator.equalsIgnoreCase(""))) {
            return (false);
        } else if ((_indicator.equalsIgnoreCase("true")) ||
                (_indicator.equalsIgnoreCase("yes"))) {
            return (true);
        } else {
            return (false);
        }
    } // End of convertStringtoBoolean Method.

    /**
     * getHumanNumber method to parse a number from a
     * human readable form.
     *
     * @param hnum of Human Readable number.
     * @return int of the numeric value.
     */
    protected int convertHumanNumber(String hnum) {
        int rnum = -1;
        int mby = 1;
        int b = 0;
        StringTokenizer st = new StringTokenizer(hnum, "kKmMgG", true);
        while (st.hasMoreTokens()) {
            switch (b) {
                case 0:
                    try {
                        rnum = Integer.parseInt(st.nextToken());
                    } catch (NumberFormatException nfe) {
                        rnum = -1;
                    } // End of Exeception.
                    b++;
                    break;

                default:
                    String LWORD = st.nextToken();
                    if (LWORD.equalsIgnoreCase("K")) {
                        mby = 1024;
                    } else if (LWORD.equalsIgnoreCase("M")) {
                        mby = (1024 * 1024);
                    } else {
                        mby = 1;
                    }
                    b++;
                    break;
            } // End of Switch.
        } // End of Tokenizer While.

        // ****************************************
        // Return the Result.
        if (rnum < 0) {
            return (rnum);
        } else {
            return (rnum * mby);
        }

    } // End of convertHumanNumber Private Method.

    /**
     * getHumanSize method to calculate the byte size into a
     * human readable form.
     *
     * @param _bytes file length in bytes.
     * @return String Human Readable form of the numeric value..
     */
    protected String getHumanSize(long _bytes) {
        String hsize = "B";
        double xb = _bytes;

        // **********************************************
        // Convert to KiloBytes, Megabytes or Gigabytes.
        //
        if (xb > 1024) {
            xb = xb / 1024;
            hsize = "K";
            if (xb > 1024) {
                xb = xb / 1024;
                hsize = "M";
                if (xb > 1024) {
                    xb = xb / 1024;
                    hsize = "G";
                } // end of Inner If.
            } // End of Outer If.
        } // End of Initial Nested If.

        // ****************************************
        // Round the Result.
        xb = xb + 0.9;
        Double XB = new Double(xb);
        long xl = XB.longValue();

        // ****************************************
        // Return the length of the file.
        return (xl + hsize);
    } // End of getHumanSize Private Method.

    /**
     * Dump Lap Timers.
     */
    public void dumpLapTimers() {

        // ***************************************
        // Show the Lap Timings.
        System.out.println("");
        System.out.println("** Timing Summary for Random Data Generator:");
        RDG.dumpLapTimers();

        System.out.println("");
        System.out.println("");
        System.out.println("** Timing Summary for Directory Operations:");

        System.out.println(" Resource Container Unit Adds: ");
        System.out.println("\t" + LP_RCU_ADDS);

        System.out.println(" Resource Adds: ");
        System.out.println("\t" + LP_REUID_ADDS);

        System.out.println(" Resource Management Layer Adds: ");
        System.out.println("\t" + LP_RMGMT_ADDS);

        System.out.println(" Resource Configuration Adds: ");
        System.out.println("\t" + LP_CEUID_ADDS);

        System.out.println(" Resource Configuration Reads: ");
        System.out.println("\t" + LP_CFG_READS);

        System.out.println(" Resource Search Identity Adds: ");
        System.out.println("\t" + LP_LRP_ADDS);

        System.out.println(" Resource UOW Adds: ");
        System.out.println("\t" + LP_UOWUID_ADDS);

        System.out.println(" ActionGroup Adds: ");
        System.out.println("\t" + LP_AGUID_ADDS);

        System.out.println("");
        System.out.println("** Timing Summary for RAW Directory Operations:");

        System.out.println(" Total LDAP ADDS: ");
        System.out.println("\t" + LP_ADDS);

        System.out.println(" Total LDAP DELETES: ");
        System.out.println("\t" + LP_DELETES);

        System.out.println(" Total LDAP MODS: ");
        System.out.println("\t" + LP_MODS);

        System.out.println(" Total LDAP SEARCHES: ");
        System.out.println("\t" + LP_SEARCH);

        System.out.println(" Total LDAP READS: ");
        System.out.println("\t" + LP_READS);


        // ****************************************
        // Dump Profile Summary Times for
        // Config Reads and Writes.
        System.out.println("");
        System.out.println("** Simulator Summary Timings per Profile:");
        Set mySet = ResourceSizeProfiles.entrySet();
        Iterator itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            idxSimulatorProfileEntry _spe =
                    (idxSimulatorProfileEntry) oit.getValue();

            System.out.println(" Profile: " + _spe.SimulatorProfileName);

            System.out.println("\tCfg Reads: " +
                    _spe.CFG_READS);

            System.out.println("\tCfg Writes: " +
                    _spe.CFG_WRITES);
        } // End of While Loop.

        // ****************************************
        // Dump Container Summary Times for
        // Config Reads and Writes.
        System.out.println("**");
        System.out.println("** Simulator Summary Timings per DIT Population:");
        mySet = ResourceDITPopulation.entrySet();
        itr = mySet.iterator();
        while (itr.hasNext()) {
            Map.Entry oit = (Map.Entry) itr.next();
            idxSimulatorDITPopulationEntry _sdp =
                    (idxSimulatorDITPopulationEntry) oit.getValue();

            // ***************************
            // Only show this which
            // have a profile assigned.
            if ((_sdp.SimulationProfileName == null) ||
                    (_sdp.SimulationProfileName.equalsIgnoreCase("")) ||
                    (_sdp.SimulationProfileName.equalsIgnoreCase("none"))) {
                continue;
            }

            // ******************************
            // Show the Timings.
            System.out.println(" Container: " + _sdp.LDAPDN + ", Profile: " +
                    _sdp.SimulationProfileName);
            System.out.println("\tCfg Reads: " +
                    _sdp.CFG_READS);

            System.out.println("\tCfg Writes: " +
                    _sdp.CFG_WRITES);

        } // End of While Loop.

    } // End of dumpLapTimers Method.                  

    /**
     * Obtain the DIT Population of the Simluation.
     */
    protected void obtainDITPopulationProperties() {
        Set ditprops = SimulatorConfig.keySet();
        Iterator itr = ditprops.iterator();
        while (itr.hasNext()) {
            String pname = (String) itr.next();
            if (!pname.startsWith("simulator.dit.rcu.below.root")) {
                continue;
            }

            // **********************************
            // We have a DIT Population Property
            String PARENT = "";
            String container = pname.substring(28);
            if ((container == null) ||
                    (container.equalsIgnoreCase(""))) {
                PARENT = DIT_ROOT;
            } else {
                PARENT = buildContainerFromPropertyName(container) +
                        "," + DIT_ROOT;
            }  // End of Else.

            // *****************************************
            // Obtain a List of Subordinates.
            LinkedList _LL = buildListFromPropertyValue(
                    SimulatorConfig.getProperty(pname));

            Iterator LL_itr = _LL.iterator();
            while (LL_itr.hasNext()) {
                String Entry = (String) LL_itr.next();
                idxParseDN _XDN =
                        new idxParseDN("rcu=" + Entry + "," + PARENT);
                if (_XDN.isValid()) {  // Save the Valid DN.
                    idxSimulatorDITPopulationEntry _sdp =
                            new idxSimulatorDITPopulationEntry();
                    _sdp.X500DN = _XDN.getX500Name();
                    _sdp.LDAPDN = _XDN.getDN();
                    _sdp.SimulationProfileName = "none";
                    ResourceDITPopulation.put(_XDN.getX500Name().toLowerCase(),
                            _sdp);
                } // End of If.
            } // End of While Loop.
        } // End of While Loop.

    } // End of obtainPopulationProperties.

    /**
     * Obtain the Resource Size Profiles of the Simluation.
     */
    protected void obtainResourceSizeProfileProperties() {
        Set ditprops = SimulatorConfig.keySet();
        Iterator itr = ditprops.iterator();
        while (itr.hasNext()) {
            String pname = (String) itr.next();
            if (!pname.startsWith("simulator.resource.size.profile.")) {
                continue;
            }

            // *****************************************
            // We have a Resource Size Profile Property
            String PROFILE_VALUE = pname.substring(32);
            int _XI = PROFILE_VALUE.indexOf(".");
            if (_XI <= 0) {
                continue;
            }
            String PROFILE_NAME = PROFILE_VALUE.substring(0, _XI);
            if ((_XI + 1) >= PROFILE_VALUE.length()) {
                continue;
            }
            PROFILE_VALUE = PROFILE_VALUE.substring(_XI + 1);

            // **********************************************
            // Get an Existing Entry.
            idxSimulatorProfileEntry SPE =
                    (idxSimulatorProfileEntry) getResourceSizeProfileEntry(PROFILE_NAME);

            if (SPE == null) {
                SPE = new idxSimulatorProfileEntry();
                SPE.SimulatorProfileName = PROFILE_NAME;
            } // End of If.

            // *********************************************
            // Now determine the type of value we have.
            if (PROFILE_VALUE.equalsIgnoreCase("initial.cfgsize")) {
                int _value = convertHumanNumber(SimulatorConfig.getProperty(pname));
                if (_value > -1) {
                    SPE.InitialConfigurationSize = _value;
                }
            } // End of If.

            else if (PROFILE_VALUE.equalsIgnoreCase("growth.cfgsize")) {
                int _value = convertHumanNumber(SimulatorConfig.getProperty(pname));
                if (_value > -1) {
                    SPE.IncrementalGrowthOfConfiguration = _value;
                }
            } // End of Else If.

            else if (PROFILE_VALUE.equalsIgnoreCase("daily.frequency")) {
                int _value = convertHumanNumber(SimulatorConfig.getProperty(pname));
                if (_value > -1) {
                    SPE.DailyFrequencyOfConfigurationChanges = _value;
                }
            } // End of Else If.

            else if (PROFILE_VALUE.equalsIgnoreCase("initial.resources")) {
                int _value = convertHumanNumber(SimulatorConfig.getProperty(pname));
                if (_value > -1) {
                    SPE.InitialNumberOfResources = _value;
                }
            } // End of Else If.

            else if (PROFILE_VALUE.equalsIgnoreCase("growth.resources")) {
                int _value = convertHumanNumber(SimulatorConfig.getProperty(pname));
                if (_value > -1) {
                    SPE.IncrementalGrowthOfResources = _value;
                }
            } // End of Else If.

            else if (PROFILE_VALUE.equalsIgnoreCase("compress.configurations")) {
                boolean _value = convertStringtoBoolean(SimulatorConfig.getProperty(pname));
                SPE.CompressConfigurations = _value;
            } // End of Else If.

            else if (PROFILE_VALUE.equalsIgnoreCase("persist.searchidentities")) {
                boolean _value = convertStringtoBoolean(SimulatorConfig.getProperty(pname));
                SPE.PersistSearchIdentities = _value;
            } // End of Else If.

            // *********************************
            // Save the Profile Object.
            ResourceSizeProfiles.put(PROFILE_NAME.toLowerCase(), SPE);

        } // End of While Loop.

    } // End of obtainResouceSizeProfileProperties.

    /**
     * Obtain the DIT Population of the Simluation.
     */
    protected void obtainProfileAssignmentProperties() {
        Set ditprops = SimulatorConfig.keySet();
        Iterator itr = ditprops.iterator();
        while (itr.hasNext()) {
            String pname = (String) itr.next();
            if (!pname.startsWith("simulator.profile.rcu.below.root")) {
                continue;
            }

            // **********************************
            // We have a DIT Population Property
            String PARENT = "";
            String container = pname.substring(32);
            if ((container == null) ||
                    (container.equalsIgnoreCase(""))) {
                PARENT = DIT_ROOT;
            } else {
                PARENT = buildContainerFromPropertyName(container) +
                        "," + DIT_ROOT;
            }  // End of Else.

            // ********************************************
            // Obtain the Container to assign the Profile.
            //
            idxParseDN _XDN = new idxParseDN(PARENT);
            if (_XDN.isValid()) {
                idxSimulatorDITPopulationEntry _sdp =
                        (idxSimulatorDITPopulationEntry) getResourceDITPopulationEntry(_XDN.getX500Name());
                if (_sdp != null) {
                    // **********************************
                    // Update the Profile to create Link.
                    _sdp.SimulationProfileName = SimulatorConfig.getProperty(pname);
                    ResourceDITPopulation.put(_XDN.getX500Name().toLowerCase(),
                            _sdp);
                } // End of If.
            } // End of If.
        } // End of While Loop.

    } // End of  obtainProfileAssignmentProperties.

    /**
     * Method used to obtain a Resource Size Profile Entry By Name.
     *
     * @param _Name of Object.
     * @return Object as Named by Name Parameter.
     */
    protected Object getResourceSizeProfileEntry(String _Name) {
        _Name = _Name.toLowerCase();
        Object Value = ResourceSizeProfiles.get(_Name);
        return (Value);
    } // End of Method

    /**
     * Method used to obtain a Resource DIT Population Entry By Name.
     *
     * @param _Name of Object.
     * @return Object as Named by Name Parameter.
     */
    protected Object getResourceDITPopulationEntry(String _Name) {
        _Name = _Name.toLowerCase();
        Object Value = ResourceDITPopulation.get(_Name);
        return (Value);
    } // End of Method

    /**
     * Method to build a LinkedList from a Property Value.
     *
     * @param str String of Comma Separated Data.
     */
    protected LinkedList<String> buildListFromPropertyValue(String str) {

        // *****************************************
        // Initialize List.
        LinkedList<String> _LIST = new LinkedList<>();

        // *******************************
        // Is this a Null or Blank Line?
        // Ignore it...
        if ((str == null) || (str.equals(""))) {
            return (_LIST);
        }

        // *******************************
        // Parse the String Data.
        StringTokenizer st = new StringTokenizer(str, ",");
        while (st.hasMoreTokens()) {
            String LWORD = st.nextToken();
            _LIST.addLast(LWORD);
        } // End of Tokenizer While.

        // ******************
        // Return.
        return (_LIST);

    } // End of buildListFromPropertyValue Method.

    /**
     * Method to build a Container Name from a Property Name.
     *
     * @param str
     * @return Formulatd LDAP Container Name.
     */
    protected String buildContainerFromPropertyName(String str) {

        // *****************************************
        // Initialize String
        String _rstr = "";

        // *******************************
        // Is this a Null or Blank Line?
        // Ignore it...
        if ((str == null) || (str.equals(""))) {
            return (_rstr);
        }

        // *******************************
        // Parse the String Data.
        StringTokenizer st = new StringTokenizer(str, ".");
        while (st.hasMoreTokens()) {
            String LWORD = st.nextToken();
            if (_rstr.length() == 0) {
                _rstr = "rcu=" + LWORD;
            } else {
                _rstr = "rcu=" + LWORD + "," + _rstr;
            }
        } // End of Tokenizer While.

        // ******************
        // Return.
        return (_rstr);

    } // End of buildListFromPropertyName Method.

    /**
     * Method to obtain an RCU or Other Container from a DN.
     *
     * @param _DN
     * @return Container Name.
     */
    protected String obtainContainer(String _DN) {

        // *****************************************
        // Check String
        if ((_DN == null) ||
                (_DN.equalsIgnoreCase(""))) {
            return ("");
        }

        // *****************************************
        // Find the First RCU or Domain Container.        
        String containerName = _DN;
        while (true) {
            idxParseDN _XDN = new idxParseDN(containerName);
            if (("rcu".equalsIgnoreCase(_XDN.getNamingAttribute())) ||
                    ("o".equalsIgnoreCase(_XDN.getNamingAttribute())) ||
                    ("ou".equalsIgnoreCase(_XDN.getNamingAttribute())) ||
                    ("dc".equalsIgnoreCase(_XDN.getNamingAttribute()))) {
                break;
            }

            containerName = _XDN.getPDN();
            if (containerName.equalsIgnoreCase("")) {
                break;
            }
        } // End of While Loop.

        // ************************
        // Return
        return (containerName);
    } // End of obtainContainer Method.

    /**
     * Perform Simulation Sequence.
     */
    protected void performSimulation() {

        // *******************************
        // Iterate through the Simulation
        // Sequence and perform each
        // phase accordingly.
        //
        boolean inLoop = false;
        int NumberOfLoops = 0;
        LinkedList<String> LoopSequence = new LinkedList<>();

        Iterator sequence = SimulationProcessSequence.iterator();
        while (sequence.hasNext()) {
            String _seqentry = (String) sequence.next();

            // ****************************************
            // Now determine the Method Name to perform
            // based upon the specified Sequence Name.
            //
            _seqentry = _seqentry.toLowerCase();

            if (_seqentry.startsWith("loop=")) {
                inLoop = true;
                NumberOfLoops = convertHumanNumber(_seqentry.substring(5));
                if (NumberOfLoops == 0) {
                    NumberOfLoops = 1;
                }
            } // End of If.

            else if (_seqentry.equalsIgnoreCase("loop")) {
                inLoop = true;
                NumberOfLoops = 1;
            } // End of If.

            else if (_seqentry.equalsIgnoreCase("loopend")) {
                // ********************
                // Now Kick off the
                // Sequence Loop.
                //
                for (int CurrentLoop = 0; CurrentLoop < NumberOfLoops; CurrentLoop++) {
                    System.out.println("**");
                    System.out.println("** Simulator Starting Sequence Loop " +
                            "Iteration:[" + (CurrentLoop + 1) + "] of [" + NumberOfLoops + "].");
                    Iterator loopitr = LoopSequence.iterator();
                    while (loopitr.hasNext()) {
                        performSimulationStep((String) loopitr.next());
                    } // End of While Loop.
                    System.out.println("** Simulator Completed Sequence Loop " +
                            "Iteration:[" + (CurrentLoop + 1) + "] of [" + NumberOfLoops + "].");
                } // End of For Loop.

                // *******************
                // Clean-Up.
                LoopSequence.clear();
                inLoop = false;
                NumberOfLoops = 0;
            } // End of Else If.

            // ***************************
            // Save a Loop Sequence Entry.
            else if (inLoop) {
                LoopSequence.addLast(_seqentry);
            }

            // ****************************
            // Perform Single Step.
            else {
                performSimulationStep(_seqentry);
            }

            // **********************
            // Flush Output Buffers
            System.out.flush();
            System.err.flush();

        } // End of while Loop.

    } // End of performSimulation Method.

    /**
     * Perform Simulation Step.
     */
    protected void performSimulationStep(String _SimStepName) {
        // ****************************************
        // Check for File Stop Trigger.
        if ((STOP_PROCESSING) ||
                (checkForFileTrigger())) {
            return;
        } // Simple Flush Process.

        // ****************************************
        // Now determine the Method Name to perform
        // based upon the specified Sequence Name.
        //
        if ((_SimStepName.equalsIgnoreCase("createcontainers")) ||
                (_SimStepName.equalsIgnoreCase("buildcontainers"))) {
            createContainers();
        } else if ((_SimStepName.equalsIgnoreCase("populate")) ||
                (_SimStepName.equalsIgnoreCase("createinitialpopulation"))) {
            createInitialPopulation();
        } else if ((_SimStepName.equalsIgnoreCase("growcfgs")) ||
                (_SimStepName.equalsIgnoreCase("growconfigurations"))) {
            growConfigurations();
        } else if ((_SimStepName.equalsIgnoreCase("grow")) ||
                (_SimStepName.equalsIgnoreCase("growresources"))) {
            growResources();
        } else if ((_SimStepName.equalsIgnoreCase("count")) ||
                (_SimStepName.equalsIgnoreCase("cnt"))) {
            count();
        } else if ((_SimStepName.equalsIgnoreCase("dip")) ||
                (_SimStepName.equalsIgnoreCase("searchdip")) ||
                (_SimStepName.equalsIgnoreCase("search"))) {
            searchDIP();
        } else {
            System.err.println("** Simulator Unknown Step Name:[" +
                    _SimStepName + "], Ignoring.");
        } // End of Else.

        // **********************
        // Flush Output Buffers
        System.out.flush();
        System.err.flush();

    } // End of performSimulationStep Method.

    /**
     * Check for a Filename Trigger.
     *
     * @return boolean Indicator to stop processing or not.
     */
    protected boolean checkForFileTrigger() {
        // ******************************
        // Check for a Stop File.
        if ((STOP_FILE_TO_WATCH != null) &&
                (!STOP_FILE_TO_WATCH.equalsIgnoreCase(""))) {
            File SFW = new File(STOP_FILE_TO_WATCH);
            if (SFW.exists()) {
                System.out.println("**");
                System.out.println("** Simulator Detected Stop File, Flushing remaining Processing.");
                System.out.flush();

                // **********************
                // Return Stopping
                // Processes.
                STOP_PROCESSING = true;
                SFW.delete();
                return (true);
            } // End of If.

        } // End of STOP File Check.

        // ******************************
        // Check for a Pause File.
        if ((PAUSE_FILE_TO_WATCH != null) &&
                (!PAUSE_FILE_TO_WATCH.equalsIgnoreCase("")) &&
                (RESUME_FILE_TO_WATCH != null) &&
                (!RESUME_FILE_TO_WATCH.equalsIgnoreCase(""))) {

            // **************************
            // Now check for a Pause.
            File PFW = new File(PAUSE_FILE_TO_WATCH);
            if (PFW.exists()) {
                System.out.println("**");
                System.out.println("** Simulator Detected Pause File, will wait for a Resume.");
                System.out.flush();

                // **********************
                // Pause until we detect
                // a Resume File.
                PFW.delete();
                while (true) {
                    try {
                        // Sleep for 15 Seconds.
                        Thread.sleep((1000 * 15));
                    } catch (InterruptedException e) {
                        ; // Noop
                    } // End of Exception.

                    // ****************************
                    // Check for a Resume File.
                    File RFW = new File(RESUME_FILE_TO_WATCH);
                    if (RFW.exists()) {
                        RFW.delete();
                        break;
                    } // End of If.
                } // End of Pause While Loop.

                // *******************************
                // Indicate we have found resume
                // File.
                System.out.println("**");
                System.out.println("** Simulator Detected Resume File, will continue Processing.");
                System.out.flush();
            } // End of If.

        } // End of PAUSE/RESUME File Check.

        // ********************************
        // Return
        return (false); // No Stoppage.

    } // End of checkForFileTrigger.

    /**
     * Extended Methods for Specific Simulation.
     */
    protected void createContainers() {
    }

    protected void createInitialPopulation() {
    }

    protected void growConfigurations() {
    }

    protected void growResources() {
    }

    protected void count() {
    }

    protected void searchDIP() {
    }

} ///:~ End of idxSimulator Class
