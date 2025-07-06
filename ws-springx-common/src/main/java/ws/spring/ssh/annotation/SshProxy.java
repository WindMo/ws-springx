package ws.spring.ssh.annotation;

import org.springframework.context.annotation.Import;
import ws.spring.constant.NetworkConstants;

import java.lang.annotation.*;

/**
 * 声明一个ssh隧道代理的注解，注解内的属性值均支持从配置获取；
 * 通常在{@link org.springframework.context.annotation.Configuration}配置类上使用
 *
 * @author WindShadow
 * @version 2025-06-29.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SshProxy.List.class)
@Import(SshProxyRegistrar.class)
public @interface SshProxy {

    String source();

    String localHost() default NetworkConstants.IP_V4_ANY_ADDR;

    String localPort() default NetworkConstants.RANDOM_PORT + "";

    String remoteHost();

    String remotePort();

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Import(SshProxyRegistrar.class)
    @interface List {

        SshProxy[] value();
    }
}
