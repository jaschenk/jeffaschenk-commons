// *****************************************************************************
// MessageConstants.java
//
// *****************************************************************************
// I N T E L L I D E N,  I N C O R P O R A T E D
// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
package jeffaschenk.commons.frameworks.cnxidx.utility.pool;

/**
 * @authors john.leichner, Jeff.Schenk
 */
public interface MessageConstants {
    // Messages should be grouped by their functional area within the application

    // Directory Resource Provider Common Messages.
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CREATE_RESOURCE
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextResourceProviderDC.0001";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_CANT_CLOSE_RESOURCE
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextResourceProviderDC.0002";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_RESOURCE_INVALID
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextResourceProviderDC.0003";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_GET_CONTEXT
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextResourceProviderDC.0004";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_RESOURCE_PROVIDER_MISSING_PROPERTY
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextResourceProviderDC.0005";

    // Common Directory POOL Messages.
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_FACTORIES_REMOVAL_FAILED
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0001";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_VALIDATE
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0002";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_PROPERTY
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0003";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_INVALID_RESOURCEPROVIDER
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0004";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_USAGETRACKER
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0005";
    public static final String ICOS_UTIL_DIRECTORY_CONTEXT_POOL_UNABLE_TO_RESET
            = "jeffaschenk.commons.frameworks.cnxidx.utility.pool.DirContextPool.0006";


} ///:~ End of MessageConstants
