package ws.spring.text.annotaion;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(EscapeConfiguration.class)
public @interface EnableEscape {
}
