package jeffaschenk.commons.constraints.annotation;

import java.lang.annotation.*;

/**
 * CheckEmailAddress,
 * Custom Validator Annotation Definition
 *
 * @author jeffaschenk@gmail.com
 *         Date: Sep 13, 2010
 *         Time: 12:49:21 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@net.sf.oval.configuration.annotation.Constraint(checkWith = CheckEmailAddressValidator.class)
@Documented
public @interface CheckEmailAddress {
    String message() default "Email Address Value is Invalid!";

    boolean nullable() default false;
}


