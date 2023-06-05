package ws.spring.web.bind;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

import javax.servlet.ServletRequest;

public class FormModelResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return !BeanUtils.isSimpleValueType(parameter.getParameterType())
                && parameter.hasParameterAnnotation(FormModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        FormModel formModel = parameter.getParameterAnnotation(FormModel.class);
        Assert.state(formModel != null, "Not found FormModel annotation");
        Assert.state(mavContainer != null, "FormModelResolver requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "FormModelResolver requires WebDataBinderFactory");

        String name = parameter.getParameterName();
        Assert.state(name != null, "ParameterName is null");
        String prefix = calculateParamPrefix(formModel, name);
        Object instance = createParameterInstance(parameter, mavContainer);

        WebDataBinder binder = binderFactory.createBinder(webRequest, instance, name);
        binder.setFieldDefaultPrefix(prefix);
        if (binder instanceof WebRequestDataBinder) {

            ((WebRequestDataBinder) binder).bind(webRequest);
        } else {

            ServletRequest request = webRequest.getNativeRequest(ServletRequest.class);
            if (request != null) {
                if (binder instanceof ExtendedServletRequestDataBinder) {
                    ((ExtendedServletRequestDataBinder) binder).bind(request);
                } else {
                    binder.bind(new ServletRequestParameterPropertyValues(request, prefix));
                }
            }
        }
        return instance;
    }

    protected final String calculateParamPrefix(FormModel formModel, String originalParamName) {

        String prefix = formModel.prefix();
        String usePrefix = StringUtils.hasText(prefix) ? prefix : originalParamName;
        String separator = formModel.separator();
        return usePrefix + separator;
    }

    protected Object createParameterInstance(MethodParameter parameter, ModelAndViewContainer mavContainer) {

        Class<?> clazz = parameter.getParameterType();
        try {
            return BeanUtils.instantiateClass(clazz);
        } catch (BeanInstantiationException ex) {
            throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
        }
    }
}
