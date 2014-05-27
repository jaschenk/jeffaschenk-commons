package jeffaschenk.commons.exceptions;

import jeffaschenk.commons.constraints.SQLConstraint;
import org.springframework.validation.Errors;

/**
 * Constraint Exception
 * Identifying the constraint which was violated for the
 * associated request.
 *
 * @author Jeff
 * @version $Id: $
 */
public class ConstraintException extends ConflictException {

    /**
     * serialVersionUID, used by JAVA.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constraint Obtain from the OUTBID SQL Constraint Lookup Service
     */
    private SQLConstraint SQLContraint;

    /**
     * Constructors
     *
     * @param errorCode a {@link java.lang.String} object.
     */
    public ConstraintException(String errorCode) {
        super(errorCode);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errorCode      a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     */
    public ConstraintException(String errorCode, String defaultMessage) {
        super(errorCode, defaultMessage);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errorCode a {@link java.lang.String} object.
     * @param args      an array of {@link java.lang.Object} objects.
     */
    public ConstraintException(String errorCode, Object[] args) {
        super(errorCode, args);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errodeCode     a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     * @param args           an array of {@link java.lang.Object} objects.
     */
    public ConstraintException(String errodeCode, String defaultMessage, Object[] args) {
        super(errodeCode, defaultMessage, args);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errors a {@link org.springframework.validation.Errors} object.
     */
    public ConstraintException(Errors errors) {
        super(errors);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param errors  a {@link org.springframework.validation.Errors} object.
     */
    public ConstraintException(String message, Errors errors) {
        super(message, errors);
    }

    /**
     * Additional Constructors, with OUTBID SQL Constraint Object Set.
     *
     * @param errorCode    a {@link java.lang.String} object.
     * @param SQLContraint a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(String errorCode, SQLConstraint SQLContraint) {
        super(errorCode);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errorCode      a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     * @param SQLContraint   a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(String errorCode, String defaultMessage, SQLConstraint SQLContraint) {
        super(errorCode, defaultMessage);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errorCode    a {@link java.lang.String} object.
     * @param args         an array of {@link java.lang.Object} objects.
     * @param SQLContraint a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(String errorCode, Object[] args, SQLConstraint SQLContraint) {
        super(errorCode, args);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errodeCode     a {@link java.lang.String} object.
     * @param defaultMessage a {@link java.lang.String} object.
     * @param args           an array of {@link java.lang.Object} objects.
     * @param SQLContraint   a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(String errodeCode, String defaultMessage, Object[] args, SQLConstraint SQLContraint) {
        super(errodeCode, defaultMessage, args);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param errors       a {@link org.springframework.validation.Errors} object.
     * @param SQLContraint a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(Errors errors, SQLConstraint SQLContraint) {
        super(errors);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * <p>Constructor for ConstraintException.</p>
     *
     * @param message      a {@link java.lang.String} object.
     * @param errors       a {@link org.springframework.validation.Errors} object.
     * @param SQLContraint a {@link jeffaschenk.commons.constraints.SQLConstraint} object.
     */
    public ConstraintException(String message, Errors errors, SQLConstraint SQLContraint) {
        super(message, errors);
        this.setSQLContraint(SQLContraint);
    }

    /**
     * Get the OUTBID SQL Constraint Object representing this Constraint Violation
     *
     * @return QNOWSQLContraint
     */
    public SQLConstraint getQNOWSQLContraint() {
        return SQLContraint;
    }

    /**
     * Set the OUTBID SQL Constraint Object representing this Constraint Violation
     *
     * @param SQLContraint the SQLContraint to set
     */
    public void setSQLContraint(SQLConstraint SQLContraint) {
        this.SQLContraint = SQLContraint;
    }

}
