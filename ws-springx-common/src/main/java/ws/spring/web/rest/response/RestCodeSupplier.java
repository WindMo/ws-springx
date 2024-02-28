package ws.spring.web.rest.response;

import org.springframework.lang.NonNull;

/**
 * 实现此接口提供一个RestCode，注：响应码不能够为null
 * <p>通常情况下，我们建议通过不同的枚举常量实现此接口，以区分和管理不同模块的业务所对应的RestCode
 *
 * @author WindShadow
 * @version 2022-06-10.
 * @see RestResponse#setCode(String)
 */

@FunctionalInterface
public interface RestCodeSupplier {

    @NonNull
    String getCode();
}
