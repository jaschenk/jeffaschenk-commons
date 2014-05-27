package jeffaschenk.commons.container.security.object;

import java.io.Serializable;
import java.util.Date;

/**
 * SecurityServiceMonitorObject
 *
 * @author jeffaschenk@gmail.com
 */
public class SecurityServiceMonitorObject implements Serializable {

    private Object principal;


    private String request;


    private String localAddress;

    private String localName;

    private int localPort;

    private String remoteAddress;

    private String remoteName;

    private int remotePort;


    private long totalAttempts = 0;

    private long totalSuccessfulAttempts = 0;

    private long totalUnsuccessfulAttempts = 0;

    private long successfulAttemptsInSequence = 0;

    private long unsuccessfulAttemptsInSequence = 0;

    private Date lastSuccessfulLogInAttempt;

    private Date lastUnsuccessfulLogInAttempt;

    private Date lastLogInAttempt;

    /**
     * Default Constructor
     *
     * @param principal
     * @param request
     * @param localAddress
     * @param localName
     * @param localPort
     * @param remoteAddress
     * @param remoteName
     * @param remotePort
     */
    public SecurityServiceMonitorObject(Object principal,
                                        String request,
                                        String localAddress,
                                        String localName,
                                        int localPort,
                                        String remoteAddress,
                                        String remoteName,
                                        int remotePort) {
        this.principal = principal;
        this.request = request;
        this.localAddress = localAddress;
        this.localName = localName;
        this.localPort = localPort;
        this.remoteAddress = remoteAddress;
        this.remoteName = remoteName;
        this.remotePort = remotePort;

        this.totalAttempts++;
        this.lastLogInAttempt = new Date();
    }

    /**
     * Default Constructor
     *
     * @param principal
     * @param request
     * @param localAddress
     * @param localName
     * @param localPort
     * @param remoteAddress
     * @param remoteName
     * @param remotePort
     * @param successful
     */
    public SecurityServiceMonitorObject(Object principal,
                                        String request,
                                        String localAddress,
                                        String localName,
                                        int localPort,
                                        String remoteAddress,
                                        String remoteName,
                                        int remotePort,
                                        boolean successful) {
        this.principal = principal;
        this.request = request;
        this.localAddress = localAddress;
        this.localName = localName;
        this.localPort = localPort;
        this.remoteAddress = remoteAddress;
        this.remoteName = remoteName;
        this.remotePort = remotePort;

        if (successful) {
            this.successfulAttemptsInSequence++;
            this.unsuccessfulAttemptsInSequence = 0;
            this.totalSuccessfulAttempts++;
            this.lastSuccessfulLogInAttempt = new Date();
        } else {
            this.successfulAttemptsInSequence = 0;
            this.unsuccessfulAttemptsInSequence++;
            this.totalUnsuccessfulAttempts++;
            this.lastUnsuccessfulLogInAttempt = new Date();
        }
    }


    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public Date getLastLogInAttempt() {
        return lastLogInAttempt;
    }

    public void setLastLogInAttempt(Date lastLogInAttempt) {
        this.lastLogInAttempt = lastLogInAttempt;
    }

    public Date getLastSuccessfulLogInAttempt() {
        return lastSuccessfulLogInAttempt;
    }

    public void setLastSuccessfulLogInAttempt(Date lastSuccessfulLogInAttempt) {
        this.lastSuccessfulLogInAttempt = lastSuccessfulLogInAttempt;
    }

    public Date getLastUnsuccessfulLogInAttempt() {
        return lastUnsuccessfulLogInAttempt;
    }

    public void setLastUnsuccessfulLogInAttempt(Date lastUnsuccessfulLogInAttempt) {
        this.lastUnsuccessfulLogInAttempt = lastUnsuccessfulLogInAttempt;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void incrementTotalAttempts() {
        this.totalAttempts++;
    }

    public long getSuccessfulAttemptsInSequence() {
        return successfulAttemptsInSequence;
    }

    public void incrementSuccessfulAttempts() {
        this.successfulAttemptsInSequence++;
    }

    public long getUnsuccessfulAttemptsInSequence() {
        return unsuccessfulAttemptsInSequence;
    }

    public void incrementUnsuccessfulAttempts() {
        this.unsuccessfulAttemptsInSequence++;
    }

    public long getTotalSuccessfulAttempts() {
        return totalSuccessfulAttempts;
    }

    public void incrementTotalSuccessfulAttempts() {
        this.totalSuccessfulAttempts++;
    }

    public long getTotalUnsuccessfulAttempts() {
        return totalUnsuccessfulAttempts;
    }

    public void incrementTotalUnsuccessfulAttempts() {
        this.totalUnsuccessfulAttempts++;
    }

    /**
     * equals override,
     * take necessary properties into consideration.
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityServiceMonitorObject)) return false;

        SecurityServiceMonitorObject that = (SecurityServiceMonitorObject) o;
        if (principal != null ? !principal.equals(that.principal) : that.principal != null) return false;
        return true;
    }

    /**
     * hashcode override,
     * take necessary properties into consideration.
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = principal != null ? principal.hashCode() : 0;
        return result;
    }

    /**
     * Override to String.
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(this.getClass().getSimpleName() + " Principal:[" + this.getPrincipal() + "], Request:[" + this.getRequest() + "], ");
        sb.append(System.getProperty("line.separator"));

        sb.append("       Total Attempts:[" + this.totalAttempts + "], Successful:[" + this.successfulAttemptsInSequence + "], Unsuccesssful:[" + this.unsuccessfulAttemptsInSequence + "].");
        sb.append(System.getProperty("line.separator"));

        sb.append("   Last Login Attempt:[" + this.lastLogInAttempt + "], Successful:[" + this.lastSuccessfulLogInAttempt + "], Unsuccesssful:[" + this.lastUnsuccessfulLogInAttempt + "]");
        sb.append(System.getProperty("line.separator"));

        sb.append(" Last Request from Remote Address:[" + this.getRemoteAddress() + "], Port:[" + this.getRemotePort() + "], Name:[" + this.getRemoteName() + "], ");
        sb.append(System.getProperty("line.separator"));
        sb.append("   To Local Address:[" + this.getLocalAddress() + "], Port:[" + this.getLocalPort() + "], Name:[" + this.getLocalName() + "], ");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    /**
     * Create an Object Key based upon essential properties.
     *
     * @return String of formulated Key.
     */
    public String toKey() {
        return this.getPrincipal().toString().toLowerCase();
    }

}
