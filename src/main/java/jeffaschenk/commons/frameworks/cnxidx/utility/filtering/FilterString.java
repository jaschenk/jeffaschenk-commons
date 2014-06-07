package jeffaschenk.commons.frameworks.cnxidx.utility.filtering;

import java.util.LinkedList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * FilterString.java
 * This Utility Class provides the ability to read an external
 * set of Filters From a File and provide Matching Tests against an
 * an incoming String.
 * Created on October 11, 2005, 11:34 AM
 *
 * @author jeff.schenk
 */
public class FilterString {

    public static final String FILTER_GROUP = "FILTERGROUP:";
    public static final String FILTER_GROUP_DEFAULT = "DEFAULT";

    // **********************************
    // Patterns to be Matched.
    private List<String> filterList = new LinkedList<>();

    // **********************************
    // Group Information and Association
    // to Filters.
    private List<String> filterGroupNameList = new LinkedList<>();
    private List<Integer> filterGroupList = new LinkedList<>();

    /**
     * Creates a new instance of FilterString
     */
    public FilterString() {
    } // End of Constructor.

    /**
     * Creates a new instance of FilterString,and load the List from a File.
     */
    public FilterString(File FilterFile) throws IOException {
        this.loadFiltersFromFile(FilterFile);
    } // End of Constructor.

    /**
     * Creates a new instance of FilterString,and load the List from a File.
     */
    public FilterString(File FilterFile, boolean groups) throws IOException {
        if (groups) {
            this.loadGroupedFiltersFromFile(FilterFile);
        } else {
            this.loadFiltersFromFile(FilterFile);
        }
    } // End of Constructor.

    /**
     * Add Entry to Match List.
     */
    public void add(String filterentry) {
        this.filterList.add(filterentry);
    } // End of add Method.

    /**
     * Return Size of Filter List.
     */
    public int size() {
        return this.filterList.size();
    } // End of size Method.

    /**
     * Match a String
     */
    public boolean match(String str) {
        boolean isMatch = false;
        Iterator itr = filterList.iterator();
        String nextValue;
        //Iterate round set now unitl we have data to match to block
        while (itr.hasNext() && !isMatch) {
            nextValue = (String) itr.next();
            isMatch = Pattern.matches(nextValue, str);
        } // End of While Loop.
        return isMatch;
    } // End of match public method.

    /**
     * Match a String and return a Group Value for this Matched Filter.
     * If no Match the String will be null.
     */
    public String obtainGroupValueWithMatch(String str) {
        boolean isMatch;
        Iterator itr = filterList.iterator();
        String nextValue;
        int current_index = 0;
        // ************************
        // Iterate Through filters
        // to obtain a match.
        while (itr.hasNext()) {
            nextValue = (String) itr.next();
            isMatch = Pattern.matches(nextValue, str);
            if (isMatch) {
                Integer groupindex = this.filterGroupList.get(current_index);
                return this.filterGroupNameList.get(groupindex);
            } // End of Match Check.
            current_index++;
        } // End of While Loop.
        return null;
    } // End of obtainGroupValueWithMatch public method.

    /**
     * Filter Loader Private Method to Load the Filter
     * from File.
     */
    private void loadFiltersFromFile(File FilterFile)
            throws IOException {

        // ******************************
        // Initialize.
        BufferedReader in
                = new BufferedReader(new FileReader(FilterFile));
        String filterline;
        // ******************************
        // Loop to Read in the Filters.
        while ((filterline = in.readLine()) != null) {
            // ********************************
            // Ignore Any Comment Lines.
            if ((filterline.trim().equalsIgnoreCase("")) ||
                    (filterline.startsWith("#"))) {
                continue;
            }

            // ********************************
            // Now Add Filter to Our Set.
            this.filterList.add(filterline);
        } // End of While Loop Loading Filter Set.
    } // End of loadFiltersFromFile Private Method.

    /**
     * Filter Loader Private Method to Load the Filter
     * from File.  This Loader has embedded property values to
     * group patterns for meaning.
     */
    private void loadGroupedFiltersFromFile(File FilterFile)
            throws IOException {

        // ******************************
        // Initialize.
        BufferedReader in
                = new BufferedReader(new FileReader(FilterFile));
        String filterline;
        int current_group = 0;
        this.filterGroupNameList.add(FilterString.FILTER_GROUP_DEFAULT);

        // ******************************
        // Loop to Read in the Filters.
        while ((filterline = in.readLine()) != null) {
            // ********************************
            // Ignore Any Comment Lines.
            if ((filterline.trim().equalsIgnoreCase("")) ||
                    (filterline.startsWith("#"))) {
                continue;
            }

            // ********************************
            // Check for a Group Designation
            if (filterline.trim().toUpperCase().startsWith(FilterString.FILTER_GROUP)) {
                this.filterGroupNameList.add(filterline.substring(FilterString.FILTER_GROUP.length()));
                current_group = (this.filterGroupNameList.size() - 1);
                // **************************
                // Continue with Next Line.
                continue;
            } // End of Check for FilterGroup.

            // ********************************
            // Now Add Filter to Our Set.
            this.filterList.add(filterline);
            this.filterGroupList.add(current_group);
        } // End of While Loop Loading Filter Set.
    } // End of loadGroupedFiltersFromFile Private Method.

} ///:~ End of FilterString Utility Class.
