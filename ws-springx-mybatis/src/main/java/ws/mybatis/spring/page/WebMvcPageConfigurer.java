package ws.mybatis.spring.page;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.List;

/**
 * @author WindShadow
 * @version 2021-12-28.
 */

public class WebMvcPageConfigurer implements WebMvcConfigurer {

    /**
     * SpringMVC默认的mapping处理器适配器{@linkplain RequestMappingHandlerAdapter#getDefaultArgumentResolvers() 配置方法参数解析器}
     * 时，用户自定义的参数解析器在{@linkplain RequestParamMethodArgumentResolver 兜底的解析器}之前，所以不必担心被截胡
     *
     * @param resolvers 方法参数解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PageHandlerMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PageHandlerInterceptor());
    }

    @Bean
    @ConditionalOnMissingBean(PagingResponseBodyAdvice.class)
    public PagingResponseBodyAdvice<?> pageDataPagingResponseBodyAdvice() {

        return new PagingResponseBodyAdvice<>();
    }
}
