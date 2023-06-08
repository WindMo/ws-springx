package ws.spring.text.annotaion;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import ws.spring.text.autoconfigure.EscapeAutoConfigure;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ImportAutoConfiguration(EscapeAutoConfigure.class)
public @interface EnableEscape {
}
