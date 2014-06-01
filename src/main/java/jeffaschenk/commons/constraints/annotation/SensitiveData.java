package jeffaschenk.commons.constraints.annotation;

import java.lang.annotation.*;

/**
 * SensitiveData
 * Custom Annotation Definition to flag certain Data
 * as Sensitive!
 *
 * @author jeffascenk@gmail.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface SensitiveData {

    boolean encrypted() default true;

}


