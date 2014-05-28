package jeffaschenk.commons.container.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Simple POJO to contain the Search Criteria for a given Web Page.
 *
 * @author jeffaschenk@gmail.com
 * @version $Id: $
 */
public class PagableSearchCriteria implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    // ***************************************
    // Logging
    /**
     * Constant <code>log</code>
     */
    protected static Log log = LogFactory.getLog(PagableSearchCriteria.class);

    /**
     * Paging Directions
     */
    public static final Integer DIRECTION_NONE = 0;
    /**
     * Constant <code>DIRECTION_BACKWARD</code>
     */
    public static final Integer DIRECTION_BACKWARD = 1;
    /**
     * Constant <code>DIRECTION_FORWARD</code>
     */
    public static final Integer DIRECTION_FORWARD = 2;
    /**
     * Constant <code>DIRECTION_GOTOPAGE</code>
     */
    public static final Integer DIRECTION_GOTOPAGE = 3;

    /**
     * String Paging Directions
     */
    public static final String SEARCH = "Search";
    /**
     * Constant <code>PREVIOUS="Previous"</code>
     */
    public static final String PREVIOUS = "Previous";
    /**
     * Constant <code>NEXT="Next"</code>
     */
    public static final String NEXT = "Next";
    /**
     * Constant <code>GOTOPAGE="GoToPage"</code>
     */
    public static final String GOTOPAGE = "GoToPage";

    /**
     * Default Maximum Rows per Page
     */
    public static final int DEFAULT_MAX_PER_PAGE = 50;

    /**
     * Name of this Search Criteria
     */
    private String name;

    /**
     * Button Clicked to Determine Direction and if a new search filter was
     * entered.
     */
    private String buttonClicked;

    /**
     * Search criteria for Page, used to Display.
     */
    private String searchCriteria;

    /**
     * Actual Search criteria for Page used to send to DAO Layer.
     */
    private String actualSearchCriteria;

    /**
     * Full Row count of Element List.
     */
    private int rowCount = 0;

    /**
     * Rows per Page
     */
    private int rowsPerPage = DEFAULT_MAX_PER_PAGE;

    /**
     * Current Page Number
     */
    private int currentPageNumber = 1;

    /**
     * Map of Row Segments
     */
    private Map<Integer, PageSegmentBounds> pageSegments = new TreeMap<Integer, PageSegmentBounds>();

    /**
     * Selected Page Number for Go To
     */
    private int selectedPageNumber = 1;

    /**
     * Available Rows Per Page
     */
    private int[] availableRowsPerPage = {10, 20, 30, 40, 50, 100, 300};

    /**
     * default Constructor
     */
    public PagableSearchCriteria() {
        super();
    }

    /**
     * Constructor with specified Name
     *
     * @param name a {@link java.lang.String} object.
     */
    public PagableSearchCriteria(String name) {
        super();
        this.name = name;
    }

    /**
     * name
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get Search criteria on Display and Entered by User
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * <p>Setter for the field <code>searchCriteria</code>.</p>
     *
     * @param searchCriteria the searchCriteria to set
     */
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
        this.actualSearchCriteria = searchCriteria;
        if ((this.actualSearchCriteria != null) &&
                (this.actualSearchCriteria.trim().length() > 0) &&
                (!this.actualSearchCriteria.trim().contains("%"))) {
            this.actualSearchCriteria = this.actualSearchCriteria + "%";
        }
    }

    /**
     * Actual Search Criteria modified for a Like Condition
     *
     * @return a {@link java.lang.String} object.
     */
    public String getActualSearchCriteria() {
        return actualSearchCriteria;
    }

    /**
     * <p>Setter for the field <code>actualSearchCriteria</code>.</p>
     *
     * @param actualSearchCriteria the actualSearchCriteria to set
     */
    protected void setActualSearchCriteria(String actualSearchCriteria) {
        this.actualSearchCriteria = actualSearchCriteria;
    }

    /**
     * buttonClicked
     *
     * @return a {@link java.lang.String} object.
     */
    public String getButtonClicked() {
        return buttonClicked;
    }

    /**
     * <p>Setter for the field <code>buttonClicked</code>.</p>
     *
     * @param buttonClicked the buttonClicked to set
     */
    public void setButtonClicked(String buttonClicked) {
        this.buttonClicked = buttonClicked;
    }

    /**
     * rowCount
     *
     * @return a int.
     */
    public int getRowCount() {
        return rowCount;
    }

    /**
     * <p>Setter for the field <code>rowCount</code>.</p>
     *
     * @param rowCount the rowCount to set
     */
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * rowsPerPage
     *
     * @return a int.
     */
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    /**
     * <p>Setter for the field <code>rowsPerPage</code>.</p>
     *
     * @param rowsPerPage the rowsPerPage to set
     */
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * pageSegments
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<Integer, PageSegmentBounds> getPageSegments() {
        return pageSegments;
    }

    /**
     * <p>Setter for the field <code>pageSegments</code>.</p>
     *
     * @param pageSegments the pageSegments to set
     */
    public void setPageSegments(Map<Integer, PageSegmentBounds> pageSegments) {
        this.pageSegments = pageSegments;
    }

    /**
     * SelectedPageNumber
     *
     * @return a int.
     */
    public int getSelectedPageNumber() {
        return selectedPageNumber;
    }

    /**
     * <p>Setter for the field <code>selectedPageNumber</code>.</p>
     *
     * @param selectedPageNumber the selectedPageNumber to set
     */
    public void setSelectedPageNumber(int selectedPageNumber) {
        this.selectedPageNumber = selectedPageNumber;
    }

    /**
     * currentPageNumber
     *
     * @return a int.
     */
    public int getCurrentPageNumber() {
        return this.currentPageNumber;
    }

    /**
     * <p>Setter for the field <code>currentPageNumber</code>.</p>
     *
     * @param currentPageNumber the currentPageNumber to set
     */
    public void setCurrentPageNumber(int currentPageNumber) {
        this.currentPageNumber = currentPageNumber;
    }

    /**
     * availableRowsPerPage
     *
     * @return an array of int.
     */
    public int[] getAvailableRowsPerPage() {
        return availableRowsPerPage;
    }

    /**
     * beginningPageRowNumber
     *
     * @return a int.
     */
    public int getBeginningPageRowNumber() {
        PageSegmentBounds currentPageSegment = this.pageSegments
                .get(new Integer(this.getCurrentPageNumber()));
        if (currentPageSegment != null) {
            return currentPageSegment.getBeginningRowNumber();
        } else {
            return 0;
        }
    }

    /**
     * endingPageRowNumber
     *
     * @return a int.
     */
    public int getEndingPageRowNumber() {
        PageSegmentBounds currentPageSegment = this.pageSegments
                .get(new Integer(this.getCurrentPageNumber()));
        if (currentPageSegment != null) {
            return currentPageSegment.getEndingRowNumber();
        } else {
            return 0;
        }
    }

    /**
     * rowsPerPage for Current Page Segment.
     *
     * @return a int.
     */
    public int getRowsPerCurrentPage() {
        PageSegmentBounds currentPageSegment = this.pageSegments.get(new Integer(this.getCurrentPageNumber()));
        if (currentPageSegment == null) {
            return this.getRowsPerPage();
        }
        return ((currentPageSegment.getEndingRowNumber() - currentPageSegment.getBeginningRowNumber()) + 1);
    }

    /**
     * Provide Number of Pages per our Segment Map.
     *
     * @return a int.
     */
    public int getNumberOfPages() {
        return this.pageSegments.size();
    }

    /**
     * Provide an array of availablePages for direct selection from a drop-down.
     *
     * @return an array of int.
     */
    public int[] getAvailablePages() {
        if (this.pageSegments == null) {
            return new int[1];
        }
        int[] availablePages = new int[this.pageSegments.size()];
        int i = 0;
        for (Integer key : this.getPageSegments().keySet()) {
            PageSegmentBounds currentPageSegment = this.pageSegments.get(key);
            availablePages[i] = currentPageSegment.getPageNumber();
            i++;
        }
        return availablePages;
    }

    /**
     * Indicator to indicate the Last Page.
     *
     * @return boolean
     */
    public boolean isLastPage() {
        PageSegmentBounds currentPageSegment = this.pageSegments
                .get(new Integer(this.getCurrentPageNumber()));
        if ((currentPageSegment != null)
                && (currentPageSegment.getPageNumber() == this.pageSegments
                .size())) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * <p/>
     * ToString Override
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Pagable Search Criteria:{ Name:[" + this.name
                + "], Criteria:[" + this.getSearchCriteria()
                + "], Button Clicked:[" + this.getButtonClicked() + "]");
        sb.append(", Row Count:[" + this.rowCount + "], Rows Per Page:["
                + this.rowsPerPage + "], Current Page Number:["
                + this.getCurrentPageNumber() + "], Selected Page Number:["
                + this.getSelectedPageNumber() + "]");
        // *************************************
        // Show Specifics for this current page
        PageSegmentBounds currentPageSegment = this.pageSegments
                .get(new Integer(this.getCurrentPageNumber()));
        if (currentPageSegment != null) {
            sb.append(", Beginning Row Number:["
                    + currentPageSegment.getBeginningRowNumber()
                    + "], Ending Row Number:["
                    + currentPageSegment.endingRowNumber + "]");
            sb.append(", Last Page:[" + this.isLastPage() + "] }");
        }
        // **************************************
        // Show All Page Segments we have.
        sb.append(this.dumpPageSegments());
        // **************************************
        // Return for Debugging Purposes.
        return sb.toString();
    }

    /**
     * Dump the Page Segments
     *
     * @return a {@link java.lang.String} object.
     */
    public String dumpPageSegments() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n*** Page Segments:\n");
        for (Integer key : this.getPageSegments().keySet()) {
            sb.append("   " + this.getPageSegments().get(key).toString()
                    + "\n");
        }
        return sb.toString();
    }

    /**
     * Helper method to generate a map of Page Segments based upon the
     * current row count and rows per page.
     */
    public void generateSegments() {
        this.pageSegments = new TreeMap<Integer, PageSegmentBounds>();
        if (this.rowCount == 0) {
            return;
        } else if (this.rowCount <= this.rowsPerPage) {
            this.pageSegments.put(new Integer(1), new PageSegmentBounds(1, 0,
                    this.rowCount - 1));
        } else if ((this.rowCount > 0) && (this.rowsPerPage > 0)) {
            double pages = ((double) this.rowCount / (double) this.rowsPerPage);
            if (pages <= 0) {
                this.pageSegments.put(new Integer(1), new PageSegmentBounds(1,
                        0, this.rowCount - 1));
            } else {
                double remainingCount = this.rowCount;
                double page = 0;
                double begin = 0;
                double end = 0;
                for (double p = 0; (p + 1) < pages; p++) {
                    page = p + 1;
                    begin = p * this.rowsPerPage;
                    end = (begin + this.rowsPerPage - 1);
                    remainingCount = remainingCount - this.rowsPerPage;
                    this.pageSegments.put(new Integer((int) page),
                            new PageSegmentBounds(page, begin, end));
                } // End of For Loop.
                // *****************************
                // Place Last Page.
                this.pageSegments.put(new Integer((int) page + 1),
                        new PageSegmentBounds(page + 1, ((end > 0) ? end + 1
                                : 0), (end + remainingCount)));
            }
        } else if (this.rowCount > 0) {
            this.pageSegments.put(new Integer(1), new PageSegmentBounds(1, 0,
                    this.rowCount - 1));
        }
    }

    /**
     * Page based upon Specified Direction
     *
     * @param direction a {@link java.lang.Integer} object.
     */
    public void page(Integer direction) {
        if (log.isTraceEnabled()) {
            log.trace("Paging: Direction:[" + direction + "]");
            log.trace("Prior Paging Operation:[" + this.toString() + "]");
        }
        switch (direction.intValue()) {
            case 0:
                // **************
                // Do not Page.
                break;
            case 1:
                // ***************************
                // Page Backward
                this.setCurrentPageNumber(this.getCurrentPageNumber() - 1);
                break;
            case 2:
                // ***************************
                // Page Forward
                this.setCurrentPageNumber(this.getCurrentPageNumber() + 1);
                break;
            case 3:
                // **************************
                // Page GoTo
                this.setCurrentPageNumber(this.getSelectedPageNumber());
                break;
            default:
                // ***************************
                // Page Forward
                this.setCurrentPageNumber(this.getCurrentPageNumber() + 1);
                break;
        } // End of Switch statement
        // **********************************************
        // Ensure Selected Page in Sync with new Current
        this.setSelectedPageNumber(this.getCurrentPageNumber());
        if (log.isTraceEnabled()) {
            log.trace("After Paging Operation:[" + this.toString() + "]");
        }
    }

    /**
     * Page based upon Specified Direction
     */
    public void page() {
        // ****************************************
        // Determine what Button was pressed.
        if ((this.buttonClicked == null)
                || (this.buttonClicked.trim().length() <= 0)) {
            page(PagableSearchCriteria.DIRECTION_NONE);
        } else if (this.buttonClicked.equalsIgnoreCase(SEARCH)) {
            // *******************************
            // Handle New search Request
            this.setSelectedPageNumber(1);
            page(PagableSearchCriteria.DIRECTION_GOTOPAGE);
        } else if (this.buttonClicked.equalsIgnoreCase(GOTOPAGE)) {
            // *******************************
            // Handle Go To Specific Page
            page(PagableSearchCriteria.DIRECTION_GOTOPAGE);
        } else if (this.buttonClicked.equalsIgnoreCase(PREVIOUS)) {
            // *******************************
            // Previous Page.
            page(PagableSearchCriteria.DIRECTION_BACKWARD);
        } else {
            // *******************************
            // Assume Next Page.
            page(PagableSearchCriteria.DIRECTION_FORWARD);
        }
    }

    /**
     * Inner Class to provide Page Segment Bounds
     *
     * @author jeffaschenk@gmail.com
     */
    public class PageSegmentBounds {

        /**
         * Page Number Relative to One.
         */
        private int pageNumber = 0;

        /**
         * Beginning Page Row Number
         */
        private int beginningRowNumber = 0;

        /**
         * Ending Page Row Number
         */
        private int endingRowNumber = 0;

        /**
         * @param pageNumber
         * @param beginningRowNumber
         * @param endingRowNumber
         */
        public PageSegmentBounds(int pageNumber, int beginningRowNumber,
                                 int endingRowNumber) {
            super();
            this.pageNumber = pageNumber;
            this.beginningRowNumber = beginningRowNumber;
            this.endingRowNumber = endingRowNumber;
        }

        /**
         * @param pageNumber
         * @param beginningRowNumber
         * @param endingRowNumber
         */
        public PageSegmentBounds(double pageNumber, double beginningRowNumber,
                                 double endingRowNumber) {
            super();
            this.pageNumber = (int) pageNumber;
            this.beginningRowNumber = (int) beginningRowNumber;
            this.endingRowNumber = (int) endingRowNumber;
        }

        /**
         * pageNumber for this segment
         */
        public int getPageNumber() {
            return pageNumber;
        }

        /**
         * @param pageNumber the pageNumber to set
         */
        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        /**
         * beginningRowNumber for this segment
         */
        public int getBeginningRowNumber() {
            return beginningRowNumber;
        }

        /**
         * @param beginningRowNumber the beginningRowNumber to set
         */
        public void setBeginningRowNumber(int beginningRowNumber) {
            this.beginningRowNumber = beginningRowNumber;
        }

        /**
         * endingRowNumber for this segment
         */
        public int getEndingRowNumber() {
            return endingRowNumber;
        }

        /**
         * @param endingRowNumber the endingRowNumber to set
         */
        public void setEndingRowNumber(int endingRowNumber) {
            this.endingRowNumber = endingRowNumber;
        }

        @Override
        public String toString() {
            return "Segment Page Number:[" + this.pageNumber + "], "
                    + "Begin Row:[" + this.beginningRowNumber + "], End Row:["
                    + this.endingRowNumber + "]";
        }

    } // End of Inner Class

    /**
     * Main Simple Test For Paging Segments
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: <row count> <rows per page>");
            System.exit(0);
        }

        for (int i = 0; i < args.length; i = i + 2) {
            int rowcount = Integer.parseInt(args[i]);
            int rowsPerPage = Integer.parseInt(args[i + 1]);

            // ********************************
            // Initialize
            PagableSearchCriteria psc = new PagableSearchCriteria();
            psc.setRowsPerPage(rowsPerPage);
            psc.setRowCount(rowcount);
            psc.generateSegments();
            System.out.println(psc);
        }

        System.out.println("Done");
        System.exit(0);
    }

}
