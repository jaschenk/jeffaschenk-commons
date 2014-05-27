package jeffaschenk.commons.standards.util;


import java.math.BigInteger;

/**
 * Generates all the links needed in the system.
 *
 * @author (asvenss) Andreas Svensson
 */
public class LinkGenerator {

    private String contextPath;

    public LinkGenerator(String contextPath) {
        this.contextPath = contextPath;
        if (contextPath.endsWith("/")) {
            this.contextPath = this.contextPath.substring(0, this.contextPath.length() - 1);
        }
    }

    /**
     * @return the URL to the Message page
     */
    public String toMessages() {
        return contextPath + "/messages";
    }

    /**
     * @param auctionSID the SID for the auction to edit
     * @return the URL to the edit auction page
     */
    public String toEditAuction(String auctionSID) {
        return contextPath + "/sell/auctions/" + auctionSID + "/edit";
    }

    /**
     * @return the URL to the edit auction page
     */
    public String toCreateAnAuction() {
        return contextPath + "/auctions/new";
    }

    /**
     * @param auctionSID the SID of the auction to show
     * @return the URL to the auction detail page
     */
    public String toAuction(String auctionSID) {
        return contextPath + "/auctions/" + auctionSID;
    }

    /**
     * @param auctionSID The auction
     * @param itemSID    the item that should be selected
     * @return the URL to the auction detail page with a specific item showing.
     */
    public String toItem(String auctionSID, String itemSID) {

        return contextPath + "/auctions/" + auctionSID + "/items/" + itemSID;
    }

    /**
     * @param registeredUserSID the SID of the registered user
     * @return the URL to the dashboard of the registered user
     */
    public String toUser(String registeredUserSID) {
        return contextPath + "/users/" + registeredUserSID + "?tab=0";
    }

    /**
     * @param registeredUserSID the SID fo the registered user
     * @param tabNumber the tab number to select on page load (starting with 0)
     * @return the URL to the registered user's page with specified tab active
     */
    public String toUser(String registeredUserSID, int tabNumber) {
        return contextPath + "/users/" + registeredUserSID + "?tab=" + tabNumber;
    }

    /**
     * @return the URL to logout
     */
    public String toLogout() {
        return contextPath + "/logout";
    }

    /**
     * @return the URL to reset the password
     */
    public String toResetPassword() {
        return contextPath + "/login/resetpassword";
    }

    /**
     * @return the URL to a user's dashboard
     */
    public String toDashboard() {
        return contextPath + "/dashboard";
    }

    /**
     * @param type auction, item or user
     * @param sid the SID
     * @return the URL to display the map modal
     */
    public String toMap(String type, String sid) {
        return contextPath + "/modals/map/" + type + "/" + sid;
    }

    /**
     * @param type auction, item or user
     * @param sid the SID
     * @return the URL to display the map modal
     */
    public String toMap(String type, BigInteger sid) {
        return contextPath + "/modals/map/" + type + "/" + sid;
    }

    /**
     * @param auctionSid an auction sid
     * @param userSid the registered user sid
     * @return the URL to the invoice page
     */
    public String toInvoice(String auctionSid, String userSid) {
        return contextPath + "/invoice/" + auctionSid + "/" + userSid;
    }

    /**
     * @param auctionSid an auction sid
     * @param itemSid the item sid
     * @return the URL to the invoice page
     */
    public String toInvoiceByItem(String auctionSid, String itemSid) {
        return contextPath + "/invoice/" + auctionSid + "/item/" + itemSid;
    }
}
