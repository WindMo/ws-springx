package ws.mybatis.spring.page;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ws.mybatis.spring.annotation.PageNumber;
import ws.mybatis.spring.annotation.PageSize;

/**
 * 为作用在Controller方法的处理器处理{@link PageNumber}注解和{@link PageSize}注解，
 * 分别将分页参数中的页码和页数据大小绑定到方法参数上
 *
 * @author WindShadow
 * @version 2021-12-29.
 */

class PageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return PageInfoHolder.supportsMethodArgumentResolver(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        if (parameter.hasParameterAnnotation(PageNumber.class)) {
            return tryConverter(PageInfoHolder.getExpectedPageNum(), parameter, webRequest, binderFactory);
        }
        if (parameter.hasParameterAnnotation(PageSize.class)) {
            return tryConverter(PageInfoHolder.getExpectedPageSize(), parameter, webRequest, binderFactory);
        }
        throw new IllegalArgumentException("Unexpected parameter resolution");
    }

    private Object tryConverter(Integer value, MethodParameter parameter, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Class<?> parameterType = parameter.getParameterType();
        if (parameterType.isAssignableFrom(Integer.class)) {
            return value;
        } else {
            WebDataBinder binder = binderFactory.createBinder(webRequest, null, parameter.getParameterName());
            return binder.convertIfNecessary(value, parameterType, parameter);
        }
    }
}
