package ws.spring.testdemo.anno;

import org.springframework.context.annotation.Import;
import ws.spring.testdemo.support.RedisServerSupport;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2024-10-18.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisServerSupport.class)
public @interface EnableEmbeddedRedis {
}
