package ws.spring.testdemo.web.controller.advice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ws.spring.web.bind.FormModelResolver;

import java.util.List;

/**
 * @author WindShadow
 * @version 2022-07-05.
 */
@Configuration
public class PracticalWebBindSupport implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(new FormModelResolver());
    }
}
