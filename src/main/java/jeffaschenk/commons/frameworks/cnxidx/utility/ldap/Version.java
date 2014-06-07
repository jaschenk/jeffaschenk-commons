// ****************************************************************************
// Version.java
//
// ****************************************************************************
// I N T E L L I D E N,  I N C O R P O R A T E D
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// The information contained herein is copyrighted by and is Confidential and
// Proprietary to FRAMEWORK Inc.  All information contained herein,
// remains and shall remain the property of FRAMEWORK Inc. and no license
// or other rights to any information contained herein, or derivatives thereof,
// is granted or implied, except as authorized by FRAMEWORK Inc. in a
// separate agreement granting such rights. Viewer of this information and all
// associated documentation agrees not to disclose any of said information
// contained herein. 
// For the purposes of this statement, Confidential and Proprietary includes,
// but is not limited to information, drawings, specifications, sketches,
// models samples, data, computer programs, documentation or know-how. 
// Confidential and Proprietary also extends to (i)  for copyrightable or
// copyrighted material, any translation, abridgement, revision or other form
// in which an existing work may be recast, transformed or adapted;
// (ii) for patented or patentable material, any improvement thereof; and
// (iii) for material which is protected by trademark, any new material
// which may be protected by copyright, patent and/or trade secret.
//
// *****************************************************************************
package jeffaschenk.commons.frameworks.cnxidx.utility.ldap;

/**
 * Version, provides DATA contants for current Product Version.
 * During IRRinstallCustomer, the "/ou=framework" System Container
 * will be flagged with the various Options below.
 *
 * @author jeff.schenk
 * @version 4.4 $Revision
 * Developed 2005
 * @since 1.0
 */
public class Version {
    // *********************************************
    // Constants.
    private static final int VERSION = 4;

    // *********************************
    // Internal Build Name.
    public static final String BuildName = "Production Build";

    // *********************************
    // FRAMEWORK Application Version
    public static final String SysApplicationVersion = "4.4";

    // **********************************************
    // FRAMEWORK IRR Directory Master Schema Version
    public static final String SysIRRMasterSchemaVersion = "4.4";

    /**
     * Constructor for Version.
     */
    public Version() {
    } // End of Constructor.

    /**
     * Standard toString Method for this Class.
     *
     * @return String Formulated Version Information.
     */
    public String toString() {
        return ("Build Name:[" +
                BuildName +
                "System Application Version:[" +
                SysApplicationVersion +
                "], IRRMasterSchemaVersion:[" +
                SysIRRMasterSchemaVersion +
                "].");
    } // End of toString Method.
} ///~: End of Version Class.
