package jeffaschenk.commons.frameworks.cnxidx.shell;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Serializable;

/**
 * Provides Object for our CheckPointPolicy.  Handles creating TXI Files
 * for obtaining status and setting existing check point policy data.
 *
 * @author jeff.schenk
 * @since 2005/11/21
 */
public class CheckPointPolicy implements Serializable {

    // *********************************
    // Global Defaults.
    public static final int Default_PolicyAChkOnModifies = 15000;
    public static final int Default_PolicyAChkQuietSecs = 0;
    public static final int Default_PolicyAChkAtTimeOfDaySecs = 3600;
    public static final int Default_PolicyAChkEverySecs = -1;
    public static final int Default_RemoveValue = -1;
    public static final String NOT_SPECIFIED
            = "Policy Not Active.";

    // *********************************
    // Global Attribute/Field Names.
    public static final String PolicyAChkOnModifies_Name
            = "PolicyAChkOnModifies";

    public static final String PolicyAChkQuietSecs_Name
            = "PolicyAChkQuietSecs";

    public static final String PolicyAChkAtTimeOfDaySecs_Name
            = "PolicyAChkAtTimeOfDaySecs";

    public static final String PolicyAChkEverySecs_Name
            = "PolicyAChkEverySecs";

    // *********************************
    // Textual Description of each
    // Policy Attribute.
    public static final String PolicyAChkOnModifies_Description
            = "Checkpoint after this number of successful modifications";

    public static final String PolicyAChkQuietSecs_Description
            = "How long the server must be \042quiet\042 " +
            "before an auto-checkpoint request will be fullfilled.";

    public static final String PolicyAChkAtTimeOfDaySecs_Description
            = "Checkpoint every day at X seconds after midnight";

    public static final String PolicyAChkEverySecs_Description
            = "Periodic checkpoint every X seconds";


    public static final String Overview_Information
            = " FRAMEWORK Automatic Check Pointing Overview\n" +
            " --------------------------------------------\n" +
            " During normal operation, the server secures changes to directory data in the journal.\n" +
            " When the server terminates cleanly, these changes are applied to the directory's\n" +
            " database volumes and the journal is emptied. This process is known as checkpointing.\n" +
            "\n" +
            " A checkpoint is performed at termination and also at the following times:\n" +
            "    * before a backup\n" +
            "    * after a server quiesce.\n" +
            "    * after search keys have been rebuilt.\n" +
            "\n" +
            " If the server terminates uncleanly, a checkpoint cannot be performed.\n" +
            " When a server is restarted after unclean termination, the database is rolled\n" +
            " back to the last checkpoint and the journal file is replayed\n" +
            " (this is known as Fast Coldstart Processing, FCP).\n" +
            " The restart time is proportional to the size of the journal,\n" +
            " so the larger the journal the longer the restart time.\n" +
            "\n" +
            " Regular checkpointing prevents excessive restart times in the event\n" +
            " of an unclean termination and therefore an administrator may want to ensure\n" +
            " that a checkpoint is being taken on a regular basis.\n" +
            " This can be achieved by configuring automatic checkpointing.\n" +
            "\n" +
            " By default, only the above set of circumstances cause checkpoints;\n" +
            " automatic checkpoints are not enabled.";

    public static final String Policy_Information_Header
            = "\n" +
            " FRAMEWORK Triggers for Automatic Checkpoints\n" +
            " ---------------------------------------------\n";

    public static final String Policy_Description_Header
            = CheckPointPolicy.Policy_Information_Header +
            " FRAMEWORK allows four policy attributes which can be used to specify\n" +
            " which triggers are to be monitored by the server to determine when a\n" +
            " checkpoint is to be performed.\n" +
            "\n" +
            " If no triggers are defined then automatic checkpoint is disabled - this is the default behaviour.\n" +
            "\n" +
            " The names and descriptions of these policy attributes are:\n";

    public static final String General_Information
            = "\n" +
            " The Directory Server tracks the numbers involved in each of the above metrics.\n" +
            " When any one of the metrics is satisfied, the server attempts a checkpoint.\n" +
            " After checkpointing (regardless of whether the checkpoint was\n" +
            " an auto-checkpoint, or was initiated by an administrator)\n" +
            " the metric counts inside the server are reset.";

    // *********************************
    // Global Fields.
    private int PolicyAChkOnModifies;
    private boolean PolicyAChkOnModifies_WasSpecified = false;

    private int PolicyAChkQuietSecs;
    private boolean PolicyAChkQuietSecs_WasSpecified = false;

    private int PolicyAChkAtTimeOfDaySecs;
    private boolean PolicyAChkAtTimeOfDaySecs_WasSpecified = false;

    private int PolicyAChkEverySecs;
    private boolean PolicyAChkEverySecs_WasSpecified = false;

    // **********************************
    // Script output for parsing and
    // presenting.
    private StringBuffer script_output_data = new StringBuffer();

    // **********************************
    // Error Indicator, tripped when
    // when detect an error obtaining
    // the Parsed Data.
    private boolean error = false;

    /**
     * Constructor.
     */
    public CheckPointPolicy() {
    } // End of Default constructor.

    /**
     * Constructor, all Policy Fields Assigned.
     */
    public CheckPointPolicy(int PolicyAChkOnModifies,
                            int PolicyAChkQuietSecs,
                            int PolicyAChkAtTimeOfDaySecs,
                            int PolicyAChkEverySecs) {

        this.PolicyAChkOnModifies = PolicyAChkOnModifies;
        if (this.PolicyAChkOnModifies != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkOnModifies_WasSpecified = true;
        } else {
            this.PolicyAChkOnModifies_WasSpecified = false;
        }

        this.PolicyAChkQuietSecs = PolicyAChkQuietSecs;
        if (this.PolicyAChkQuietSecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkQuietSecs_WasSpecified = true;
        } else {
            this.PolicyAChkQuietSecs_WasSpecified = false;
        }


        this.PolicyAChkAtTimeOfDaySecs
                = PolicyAChkAtTimeOfDaySecs;
        if (this.PolicyAChkAtTimeOfDaySecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = true;
        } else {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = false;
        }

        this.PolicyAChkEverySecs = PolicyAChkEverySecs;
        if (this.PolicyAChkEverySecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkEverySecs_WasSpecified = true;
        }

    } // End of Default constructor.

    /**
     * Constructor, all Policy Fields Assigned, except
     * for PolicyAChkEverySecs, which is actually deprecated.
     */
    public CheckPointPolicy(int PolicyAChkOnModifies,
                            int PolicyAChkQuietSecs,
                            int PolicyAChkAtTimeOfDaySecs) {

        this.PolicyAChkOnModifies = PolicyAChkOnModifies;
        if (this.PolicyAChkOnModifies != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkOnModifies_WasSpecified = true;
        } else {
            this.PolicyAChkOnModifies_WasSpecified = false;
        }

        this.PolicyAChkQuietSecs = PolicyAChkQuietSecs;
        if (this.PolicyAChkQuietSecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkQuietSecs_WasSpecified = true;
        } else {
            this.PolicyAChkQuietSecs_WasSpecified = false;
        }

        this.PolicyAChkAtTimeOfDaySecs
                = PolicyAChkAtTimeOfDaySecs;
        if (this.PolicyAChkAtTimeOfDaySecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = true;
        } else {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = false;
        }

        this.PolicyAChkEverySecs = CheckPointPolicy.Default_RemoveValue;
        this.PolicyAChkQuietSecs_WasSpecified = false;

    } // End of Default constructor.

    /**
     * Constructor, all Policy Fields Assigned, except
     * for PolicyAChkEverySecs, which is actually deprecated.
     */
    public CheckPointPolicy(TXFile script_output,
                            TXFile script_error)
            throws IOException,
            FileNotFoundException {

        // **************************************
        // First Check for Existence of Error
        // Output.
        if (script_error.exists()) {
            if (script_output.exists()) {
                this.script_output_data.append(script_output.get());
            }
            this.script_output_data.append(script_error.get());
            this.error = true;
        } // End of Check for Script Errors.

        // **************************************
        // If did not have an Error, we need to
        // parse the Check Point Data
        // and make it available.
        else {
            if (script_output.exists()) {
                this.script_output_data.append(script_output.get());
                this.parse();
            } else {

                this.PolicyAChkOnModifies
                        = CheckPointPolicy.Default_RemoveValue;

                this.PolicyAChkQuietSecs
                        = CheckPointPolicy.Default_RemoveValue;

                this.PolicyAChkAtTimeOfDaySecs
                        = CheckPointPolicy.Default_RemoveValue;

                this.PolicyAChkEverySecs
                        = CheckPointPolicy.Default_RemoveValue;
            } // End of Inner Else.
        } // End of Else.
    } // End of Default constructor.

    /**
     * Obtain the Saved Script output
     * Data, this can be blank.
     *
     * @return String of Output Data.
     */
    public String getSavedScriptOutput() {
        return this.script_output_data.toString();
    } // End of getSavedScriptOutput.

    /**
     * Indicates if an error was detected.
     *
     * @return boolean indicator.
     */
    public boolean wasErrorDetected() {
        return this.error;
    } // End of error.

    /**
     * Obtain PolicyAChkOnModifies Value.
     *
     * @return int of PolicyAChkOnModifies
     */
    public int getPolicyAChkOnModifies() {
        return this.PolicyAChkOnModifies;
    } // End of gettter.

    /**
     * Set PolicyAChkOnModifies Value.
     *
     * @param PolicyAChkOnModifies
     */
    public void setPolicyAChkOnModifies(int PolicyAChkOnModifies) {
        this.PolicyAChkOnModifies = PolicyAChkOnModifies;
    } // End of settter.

    /**
     * Obtain PolicyAChkOnModifies Indicator.
     *
     * @return boolean of PolicyAChkOnModifies_WasSpecified
     */
    public boolean wasPolicyAChkOnModifiesSpecified() {
        return this.PolicyAChkOnModifies_WasSpecified;
    } // End of gettter.

    /**
     * Get PolicyAChkQuietSecs Value.
     *
     * @return int of PolicyAChkQuietSecs.
     */
    public int getPolicyAChkQuietSecs() {
        return this.PolicyAChkQuietSecs;
    } // End of getter.

    /**
     * Set PolicyAChkQuietSecs Value.
     *
     * @param PolicyAChkQuietSecs
     */
    public void setPolicyAChkQuietSecs(int PolicyAChkQuietSecs) {
        this.PolicyAChkQuietSecs = PolicyAChkQuietSecs;
    } // End of settter.

    /**
     * Obtain PolicyAChkQuietSecs Indicator.
     *
     * @return boolean of PolicyAChkQuietSecs_WasSpecified
     */
    public boolean wasPolicyAChkQuietSecsSpecified() {
        return this.PolicyAChkQuietSecs_WasSpecified;
    } // End of gettter.

    /**
     * Get PolicyAChkAtTimeOfDaySecs Value.
     *
     * @return int of PolicyAChkAtTimeOfDaySecs
     */
    public int getPolicyAChkAtTimeOfDaySecs() {
        return this.PolicyAChkAtTimeOfDaySecs;
    } // End of getter.

    /**
     * Set PolicyAChkAtTimeOfDaySecs Value.
     *
     * @param PolicyAChkAtTimeOfDaySecs
     */
    public void setPolicyAChkAtTimeOfDaySecs(int PolicyAChkAtTimeOfDaySecs) {
        this.PolicyAChkAtTimeOfDaySecs = PolicyAChkAtTimeOfDaySecs;
    } // End of settter.

    /**
     * Obtain PolicyAChkAtTimeOfDaySec Indicator.
     *
     * @return boolean of PolicyAChkAtTimeOfDaySec_WasSpecified
     */
    public boolean wasPolicyAChkAtTimeOfDaySecsSpecified() {
        return this.PolicyAChkAtTimeOfDaySecs_WasSpecified;
    } // End of gettter.

    /**
     * Get PolicyAChkEverySecs Value.
     *
     * @return int of PolicyAChkEverySecs.
     */
    public int getPolicyAChkEverySecs() {
        return this.PolicyAChkEverySecs;
    } // End of getter.

    /**
     * Set PolicyAChkEverySecs Value.
     *
     * @param PolicyAChkEverySecs
     */
    public void setPolicyAChkEverySecs(int PolicyAChkEverySecs) {
        this.PolicyAChkEverySecs = PolicyAChkEverySecs;
    } // End of settter.

    /**
     * Obtain PolicyAChkEverySecs Indicator.
     *
     * @return boolean of PolicyAChkEverySecs_WasSpecified
     */
    public boolean wasPolicyAChkEverySecsSpecified() {
        return this.PolicyAChkEverySecs_WasSpecified;
    } // End of gettter.

    /**
     * Resets all settings to their respective defaults.
     */
    public void reset() {
        this.PolicyAChkOnModifies
                = CheckPointPolicy.Default_PolicyAChkOnModifies;

        this.PolicyAChkQuietSecs
                = CheckPointPolicy.Default_PolicyAChkQuietSecs;

        this.PolicyAChkAtTimeOfDaySecs
                = CheckPointPolicy.Default_PolicyAChkAtTimeOfDaySecs;

        this.PolicyAChkEverySecs
                = CheckPointPolicy.Default_PolicyAChkEverySecs;
    } // End of rset Method.

    /**
     * getReadScript Get the Read Script to obtain
     * current values.
     *
     * @return String Output of Generated Read Script.
     */
    public static String getReadScript() {
        StringBuffer sb = new StringBuffer();

        sb.append("/*****************************************************************************/\n");
        sb.append("/* Read Directory CheckPoint Policy Settings                                 */\n");
        sb.append("/*****************************************************************************/\n");
        sb.append(" Read		Root\n");
        sb.append("   " + CheckPointPolicy.PolicyAChkOnModifies_Name + "\n");
        sb.append("   " + CheckPointPolicy.PolicyAChkQuietSecs_Name + "\n");
        sb.append("   " + CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name + "\n");
        sb.append("   " + CheckPointPolicy.PolicyAChkEverySecs_Name + "\n");

        // *************************
        // Return Generated Script.
        return sb.toString();
    } // End of getReadScript.

    /**
     * getModifyScript Get the Modify Script based upon out current
     * values and formulates a script to be run by a Shell.
     *
     * @return String Output of Generated Modification Script.
     */
    public String getSetScript() {
        StringBuffer sb = new StringBuffer();

        sb.append("/*****************************************************************************/\n");
        sb.append("/* Modify Current Check Point Policy                                         */\n");
        sb.append("/*****************************************************************************/\n");
        sb.append(" ModifyDSE		Root\n");


        if (this.PolicyAChkOnModifies_WasSpecified) {
            sb.append("   RemoveAttribute   " + CheckPointPolicy.PolicyAChkOnModifies_Name + "\n");
        }

        if (this.PolicyAChkQuietSecs_WasSpecified) {
            sb.append("   RemoveAttribute   " + CheckPointPolicy.PolicyAChkQuietSecs_Name + "\n");
        }

        if (this.PolicyAChkAtTimeOfDaySecs_WasSpecified) {
            sb.append("   RemoveAttribute   " + CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name + "\n");
        }

        if (this.PolicyAChkEverySecs_WasSpecified) {
            sb.append("   RemoveAttribute   " + CheckPointPolicy.PolicyAChkEverySecs_Name + "\n");
        }

        if (this.PolicyAChkOnModifies != CheckPointPolicy.Default_RemoveValue) {
            sb.append("   AddAttribute      " + CheckPointPolicy.PolicyAChkOnModifies_Name + " " +
                    Integer.toString(this.PolicyAChkOnModifies) +
                    "\n");
        } // End of If Check for Adding Attribute.

        if (this.PolicyAChkQuietSecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append("   AddAttribute      " + CheckPointPolicy.PolicyAChkQuietSecs_Name + " " +
                    Integer.toString(this.PolicyAChkQuietSecs) +
                    "\n");
        } // End of If Check for Adding Attribute.

        if (this.PolicyAChkAtTimeOfDaySecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append("   AddAttribute      " + CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name + " " +
                    Integer.toString(this.PolicyAChkAtTimeOfDaySecs) +
                    "\n");
        } // End of If Check for Adding Attribute.

        if (this.PolicyAChkEverySecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append("   AddAttribute      " + CheckPointPolicy.PolicyAChkEverySecs_Name + " " +
                    Integer.toString(this.PolicyAChkEverySecs) +
                    "\n");
        } // End of If Check for Adding Attribute.

        // *************************
        // Return Generated Script.
        return sb.toString();
    } // End of getModifyScript.

    /**
     * toString Override.  Produces proper FRAMEWORK Output.
     *
     * @return String Output to be Displayed.
     */
    public static String getPolicyDescriptions() {
        StringBuffer sb = new StringBuffer();

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkOnModifies_Name) + " - ");
        sb.append(CheckPointPolicy.PolicyAChkOnModifies_Description + "\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkQuietSecs_Name) + " - ");
        sb.append(CheckPointPolicy.PolicyAChkQuietSecs_Description + "\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name) + " - ");
        sb.append(CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Description + "\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkEverySecs_Name) + " - ");
        sb.append(CheckPointPolicy.PolicyAChkEverySecs_Description + "\n");

        // *************************
        // Return Generated Script.
        return sb.toString();
    } // End of getPolicyDescriptions Method.

    /**
     * toString Override.  Produces proper FRAMEWORK Output.
     *
     * @return String Output to be Displayed.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(CheckPointPolicy.Policy_Information_Header);

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkOnModifies_Name) + " = ");
        if (this.PolicyAChkOnModifies != CheckPointPolicy.Default_RemoveValue) {
            sb.append(Integer.toString(this.PolicyAChkOnModifies));
        } else {
            sb.append(CheckPointPolicy.NOT_SPECIFIED);
        } // End of Else.
        sb.append("\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkQuietSecs_Name) + " = ");
        if (this.PolicyAChkQuietSecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append(Integer.toString(this.PolicyAChkQuietSecs));
        } else {
            sb.append(CheckPointPolicy.NOT_SPECIFIED);
        } // End of Else.
        sb.append("\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name) + " = ");
        if (this.PolicyAChkAtTimeOfDaySecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append(Integer.toString(this.PolicyAChkAtTimeOfDaySecs));
        } else {
            sb.append(CheckPointPolicy.NOT_SPECIFIED);
        } // End of Else.
        sb.append("\n");

        sb.append("  " + CheckPointPolicy.formatName(CheckPointPolicy.PolicyAChkEverySecs_Name) + " = ");
        if (this.PolicyAChkEverySecs != CheckPointPolicy.Default_RemoveValue) {
            sb.append(Integer.toString(this.PolicyAChkEverySecs));
        } else {
            sb.append(CheckPointPolicy.NOT_SPECIFIED);
        } // End of Else.
        sb.append("\n");

        // *************************
        // Return Generated Script.
        return sb.toString();
    } // End of toString.

    /**
     * Equality must be implemented in terms of all Policy Fields.
     */
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (!(other instanceof CheckPointPolicy))
            return false;

        CheckPointPolicy cp = (CheckPointPolicy) other;
        return ((this.PolicyAChkOnModifies == cp.getPolicyAChkOnModifies())
                && (this.PolicyAChkQuietSecs == cp.getPolicyAChkQuietSecs())
                && (this.PolicyAChkAtTimeOfDaySecs == cp.getPolicyAChkAtTimeOfDaySecs())
                && (this.PolicyAChkEverySecs == cp.getPolicyAChkEverySecs()));
    } // End of Equals Method.

    /**
     * Hashcode must also depend on Policy Field values.
     */
    public int hashCode() {
        return (new Integer(this.PolicyAChkOnModifies).hashCode())
                + (new Integer(this.PolicyAChkQuietSecs).hashCode())
                + (new Integer(this.PolicyAChkAtTimeOfDaySecs).hashCode())
                + (new Integer(this.PolicyAChkEverySecs).hashCode())
                % Integer.MAX_VALUE;
    } // end of hashcode method.

    // *********************************************
    // P R I V A T E   H E L P E R S
    // *********************************************

    /**
     * This is Sample Output of what we may have to parse:
     * <p/>
     * Script executed at 13:58.48 22 Nov 2005 -08:00
     * <p/>
     * Bind successful.
     * Session established to DSA /o=icosdsa/cn=DCDSERVER/cn=camiwjas
     * <p/>
     * ChangeContext successful.
     * <p/>
     * <p/>
     * ReadResult                              Root
     * PolicyAChkQuietSecs                 1800
     * PolicyAChkEverySecs                 1800
     */
    private void parse() {
        this.PolicyAChkOnModifies =
                this.parsePolicy(CheckPointPolicy.PolicyAChkOnModifies_Name);
        if (this.PolicyAChkOnModifies != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkOnModifies_WasSpecified = true;
        } else {
            this.PolicyAChkOnModifies_WasSpecified = false;
        }

        this.PolicyAChkQuietSecs =
                this.parsePolicy(CheckPointPolicy.PolicyAChkQuietSecs_Name);
        if (this.PolicyAChkQuietSecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkQuietSecs_WasSpecified = true;
        } else {
            this.PolicyAChkQuietSecs_WasSpecified = false;
        }

        this.PolicyAChkAtTimeOfDaySecs =
                this.parsePolicy(CheckPointPolicy.PolicyAChkAtTimeOfDaySecs_Name);
        if (this.PolicyAChkAtTimeOfDaySecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = true;
        } else {
            this.PolicyAChkAtTimeOfDaySecs_WasSpecified = false;
        }

        this.PolicyAChkEverySecs =
                this.parsePolicy(CheckPointPolicy.PolicyAChkEverySecs_Name);
        if (this.PolicyAChkEverySecs != CheckPointPolicy.Default_RemoveValue) {
            this.PolicyAChkEverySecs_WasSpecified = true;
        } else {
            this.PolicyAChkEverySecs_WasSpecified = false;
        }
    } // End of parse Method.

    /**
     * Parse a Policy out of String Textual Output Data generated
     * from a Data Connection Directory Utility.
     *
     * @param policyname
     * @return int
     */
    private int parsePolicy(final String policyname) {

        // *************************************
        // Find the name of the Policy in the
        // output data.
        int x =
                this.script_output_data.indexOf(policyname);
        if (x <= 0) {
            return CheckPointPolicy.Default_RemoveValue;
        }

        // *************************************
        // Now acquire the Line it is on.
        int y = this.script_output_data.indexOf("\n", x);
        if (y <= 0) {
            return CheckPointPolicy.Default_RemoveValue;
        }

        String data = this.script_output_data.substring(x, y - 1);
        if (data.length() <= policyname.length()) {
            return CheckPointPolicy.Default_RemoveValue;
        }

        // **************************************
        // Now obtain the Data Value.
        data = data.substring((policyname.length() + 1)).trim();
        if ((data == null) ||
                (data.equalsIgnoreCase(""))) {
            return CheckPointPolicy.Default_RemoveValue;
        }
        try {
            return Integer.parseInt(data);
        } catch (NumberFormatException nfe) {
            return CheckPointPolicy.Default_RemoveValue;
        } // End of Exception Clause.
    } // End of findPolicyStringData

    /**
     * Private Method to Format Name.
     *
     * @param name
     * @return String
     */
    private static String formatName(final String name) {
        if (name == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(name);
        while (sb.length() < 25) {
            sb.append(' ');
        }
        return sb.toString();
    } // End of formatName Private Method.

} ///:~ End of CheckPointPolicy Object Class.
