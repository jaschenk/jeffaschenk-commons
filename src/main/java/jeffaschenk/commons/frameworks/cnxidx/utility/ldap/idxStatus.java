package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;


/**
 * Java Class for accumulating Status and Statistics among various
 * JNDI Functions and Class.
 *
 * @author jeff.schenk
 * @version 1.0 $Revision
 * Developed 2001
 */

public class idxStatus {

    private int DeletedEntries = 0;
    private int NonDeletableEntries = 0;

    private int ReboundEntries = 0;
    private int NonReboundableEntries = 0;

    private int AddedEntries = 0;

    private int ReadEntries = 0;

    private int DeleteErrors = 0;
    private int ReboundErrors = 0;

    private int AddErrors = 0;

    private int FluidDNErrors = 0;
    private int FluidDNModifications = 0;
    private int FluidDNDetections = 0;
    private int FluidDNDomainTraversals = 0;
    private int FluidDNChildTraversals = 0;

    private int OtherErrors = 0;

    private int CurrentOperationStatus = 0;

    private String CurrentOperationName = null;

    private int LastOperationStatus = 0;

    private String LastOperation = null;

    private String LastOperationResource = null;

    /**
     * Provides Common Status for MAIN Operation Functions.
     *
     * @param _Operation A Friendly Operation Name.
     */
    public idxStatus(String _Operation) {
        CurrentOperationName = _Operation;
    } // End of idxStatus Constructor.

    /**
     * Set Current Operation Status
     */
    public void setOpStatus(int _status) {
        CurrentOperationStatus = _status;
    } // End of setOpStatus Method.

    /**
     * Get Current Operation Status
     */
    public int getOpStatus() {
        return (CurrentOperationStatus);
    } // End of setOpStatus Method.

    /**
     * Set Current Operation Name
     */
    public void setOpName(String _Operation) {
        CurrentOperationName = _Operation;
    } // End of setOpName Method.

    /**
     * Get Current Operation Status
     */
    public String getOpName() {
        return (CurrentOperationName);
    } // End of getOpName Method.

    /**
     * Set Last Operation
     */
    public void setLastOp(String _Operation) {
        LastOperation = _Operation;
    } // End of setLastOp Method.

    /**
     * Get Last Operation
     */
    public String getLastOp() {
        return (LastOperation);
    } // End of getLastOpStatus Method.

    /**
     * Set Last Status
     */
    public void setLastOpStatus(int _OpStat) {
        LastOperationStatus = _OpStat;
    } // End of setLastOpStatus Method.

    /**
     * Get Last Operation Status
     */
    public int getLastOpStatus() {
        return (LastOperationStatus);
    } // End of getLastOpStatus Method.

    /**
     * Set Last Operation Resource
     */
    public void setLastOpResource(String _Name) {
        LastOperationResource = _Name;
    } // End of setLastOpResource Method.

    /**
     * Get Last Operation
     */
    public String getLastOpResource() {
        return (LastOperationResource);
    } // End of getLastOpResource Method.


    /**
     * Accumulates Entry Counter
     */
    public void AccumCounter(String _CName) {
        if ("DeletedEntries".equalsIgnoreCase(_CName)) {
            DeletedEntries++;
        } else if ("NonDeletableEntries".equalsIgnoreCase(_CName)) {
            NonDeletableEntries++;
        } else if ("ReboundEntries".equalsIgnoreCase(_CName)) {
            ReboundEntries++;
        } else if ("NonReboundableEntries".equalsIgnoreCase(_CName)) {
            NonReboundableEntries++;
        } else if ("AddedEntries".equalsIgnoreCase(_CName)) {
            AddedEntries++;
        } else if ("ReadEntries".equalsIgnoreCase(_CName)) {
            ReadEntries++;
        } else if ("FluidDNDetections".equalsIgnoreCase(_CName)) {
            FluidDNDetections++;
        } else if ("FluidDNModifications".equalsIgnoreCase(_CName)) {
            FluidDNModifications++;
        } else if ("FluidDNDomainTraversals".equalsIgnoreCase(_CName)) {
            FluidDNDomainTraversals++;
        } else if ("FluidDNChildTraversals".equalsIgnoreCase(_CName)) {
            FluidDNChildTraversals++;
        } else if ("DeleteErrors".equalsIgnoreCase(_CName)) {
            DeleteErrors++;
        } else if ("ReboundErrors".equalsIgnoreCase(_CName)) {
            ReboundErrors++;
        } else if ("AddErrors".equalsIgnoreCase(_CName)) {
            AddErrors++;
        } else if ("FluidDNErrors".equalsIgnoreCase(_CName)) {
            FluidDNErrors++;
        } else if ("OtherErrors".equalsIgnoreCase(_CName)) {
            OtherErrors++;
        }
    } // End of AccumCounter Method.

    /**
     * Returns Current Counter Statistic.
     *
     * @return int of counter.
     */
    public int getCounter(String _CName) {
        if ("DeletedEntries".equalsIgnoreCase(_CName)) {
            return (DeletedEntries);
        } else if ("NonDeletableEntries".equalsIgnoreCase(_CName)) {
            return (NonDeletableEntries);
        } else if ("ReboundEntries".equalsIgnoreCase(_CName)) {
            return (ReboundEntries);
        } else if ("NonReboundableEntries".equalsIgnoreCase(_CName)) {
            return (NonReboundableEntries);
        } else if ("AddedEntries".equalsIgnoreCase(_CName)) {
            return (AddedEntries);
        } else if ("ReadEntries".equalsIgnoreCase(_CName)) {
            return (ReadEntries);
        } else if ("FluidDNDetections".equalsIgnoreCase(_CName)) {
            return (FluidDNDetections);
        } else if ("FluidDNModifications".equalsIgnoreCase(_CName)) {
            return (FluidDNModifications);
        } else if ("FluidDNDomainTraversals".equalsIgnoreCase(_CName)) {
            return (FluidDNDomainTraversals);
        } else if ("FluidDNChildTraversals".equalsIgnoreCase(_CName)) {
            return (FluidDNChildTraversals);
        } else if ("DeleteErrors".equalsIgnoreCase(_CName)) {
            return (DeleteErrors);
        } else if ("ReboundErrors".equalsIgnoreCase(_CName)) {
            return (ReboundErrors);
        } else if ("AddErrors".equalsIgnoreCase(_CName)) {
            return (AddErrors);
        } else if ("OtherErrors".equalsIgnoreCase(_CName)) {
            return (OtherErrors);
        } else if ("FluidDNErrors".equalsIgnoreCase(_CName)) {
            return (FluidDNErrors);
        } else {
            return (0);
        }
    } // End of getCounter Method.

    /**
     * Show All Statistics
     */
    public void show() {
        System.out.println("\n# ***********************************" +
                "***********************************");

        System.out.println("Statistics for " +
                CurrentOperationName + ":");

        System.out.println("\tDeleted Entries: ....... " +
                DeletedEntries);

        System.out.println("\tNon-Deletable Entries: . " +
                NonDeletableEntries);

        System.out.println("\tRebound Entries: ....... " +
                ReboundEntries);

        System.out.println("\tNon-Reboundable Entries: " +
                NonReboundableEntries);

        System.out.println("\tAdded Entries: ......... " +
                AddedEntries);

        System.out.println("\tRead Entries: .......... " +
                ReadEntries);

        System.out.println("\tFluid DN Detections: ..  " +
                FluidDNDetections);

        System.out.println("\tFluid DN Modifications:  " +
                FluidDNModifications);

        System.out.println("\t FDN Domain Traversals:  " +
                FluidDNDomainTraversals);

        System.out.println("\t FDN Child Traversals: . " +
                FluidDNChildTraversals);

        System.out.println("\tDelete Errors: ......... " +
                DeleteErrors);

        System.out.println("\tRebound Errors: ........ " +
                ReboundErrors);

        System.out.println("\tAdd Errors: ............ " +
                AddErrors);

        System.out.println("\tFluid DN Errors: ....... " +
                FluidDNErrors);

        System.out.println("\tOther Errors: .......... " +
                OtherErrors);

        System.out.println("Current Status for " +
                CurrentOperationName + ":[" +
                CurrentOperationStatus + "]");

        System.out.println("# ***********************************" +
                "***********************************");
    } // End of show Method.


} ///:~ End of idxStatus Class.
