package ws.spring.context.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 精准配置注解，可用此注解声明常规的配置类（注意{@link Configuration#proxyBeanMethods()}设置为false），
 * 但是它们不会被来自{@linkplain org.springframework.boot.autoconfigure.SpringBootApplication SpringBoot应用}的默认扫描策略扫描进IOC容器中。
 * 通常用于更精准的配置引入，如使用{@link org.springframework.context.annotation.Import}注解注入，以实现特定的配置开关时，
 * 可以使用此注解声明配置类而不必担心它们被默认的组件扫描策略给引入。
 *
 * @author WindShadow
 * @version 2025-06-23.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration(proxyBeanMethods = false)
public @interface ExactConfiguration {

    @AliasFor(annotation = Configuration.class)
    String value() default "";
}
