package jeffaschenk.commons.constraints.annotation;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * String UTF8StringLength Check
 * Provides true check of String when UTF-8 characters could be contained within
 * the String.
 *
 * @author jeffaschenk@gmail.com
 * @see UTF8StringLength Annotation
 */
public class UTF8StringLengthCheck extends AbstractAnnotationCheck<UTF8StringLength> {

    private int min;
    private int max;
    private int length;

    /**
     * Configure the Represented Annotation Instance.
     *
     * @param utf8StringLength
     */
    public void configure(UTF8StringLength utf8StringLength) {
        min = utf8StringLength.min();
        max = utf8StringLength.max();
        this.setMessage(utf8StringLength.message());
    }

    /**
     * Override of the isSatisfied Method to properly check field length/sizes when
     * UTF-8 Characters are involved in checking field lengths.
     * <p/>
     * Normal String length check, only provides you how many uni-codes are
     * present in the String.  But the actual length of the string could be a lot longer
     * as each UTF-8 character could take up an additional Byte to fully realize the true
     * character externally from JAVA, such as when we persist data to a DB.
     *
     * @param validatedObject
     * @param valueToValidate
     * @param context
     * @param validator
     * @return
     */
    public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator) {
        this.requireMessageVariablesRecreation();
        if (valueToValidate == null) {
            return true;
        }
        if (!String.class.isInstance(valueToValidate)) {
            return true;
        }
        try {
            byte[] bytes = ((String) valueToValidate).getBytes("UTF-8");
            this.length = bytes.length;
            return ((this.length >= min) && (this.length <= max));
        } catch (UnsupportedEncodingException uee) {
            // Ignore...
        }
        return false;
    }

    @Override
    public Map<String, String> createMessageVariables() {
        Map<String, String> messageVariables = new HashMap<String, String>(3);
        messageVariables.put("min", Integer.toString(min));
        messageVariables.put("max", Integer.toString(max));
        messageVariables.put("length", Integer.toString(length));
        return messageVariables;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
