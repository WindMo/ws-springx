package ws.spring.testdemo.anno;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2024-10-18.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisServerConfiguration.class)
public @interface EnableEmbeddedRedis {
}
