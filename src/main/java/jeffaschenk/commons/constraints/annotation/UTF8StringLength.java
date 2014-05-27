package jeffaschenk.commons.constraints.annotation;

import java.lang.annotation.*;

/**
 * True UTF8StringLength Checker for String
 *
 * @author jeffaschenk@gmail.com
 * @see UTF8StringLengthCheck
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@net.sf.oval.configuration.annotation.Constraint(checkWith = UTF8StringLengthCheck.class)
@Documented
public @interface UTF8StringLength {

    public int min() default 0;

    public int max() default 2147483647;

    public String message() default "Invalid Length of {length}, should be {min}..{max}";
}
