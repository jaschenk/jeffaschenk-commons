package jeffaschenk.commons.frameworks.cnxidx.admin;

/*
 * IRRBackupSearchResult.java
 *
 * Created on October 6, 2005, 6:29 PM
 *
 * @author jeff.schenk
 */

import javax.naming.directory.SearchResult;

/**
 * IRRBackupSearchResult Wraps the actual SearchResult and the
 * Stringified Distinguished Name, since the Name does not exist
 * in the search since this was an Object driven Search.
 */
public class IRRBackupSearchResult {
    protected String DistinguishedName;
    protected SearchResult searchresult;

    /**
     * Creates a new instance of IRRBackupSearchResult
     */
    public IRRBackupSearchResult(String DistinguishedName, SearchResult searchresult) {
        this.DistinguishedName = DistinguishedName;
        this.searchresult = searchresult;
    } // End of Constructor.
} ///:~ End of IRRBackupSearchResult Class.
