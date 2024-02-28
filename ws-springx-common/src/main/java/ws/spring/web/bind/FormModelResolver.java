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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FormModelResolver implements HandlerMethodArgumentResolver {

    private final Map<MethodParameter, FormModelInfo> formModelInfoCache = new ConcurrentHashMap<>(256);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return !BeanUtils.isSimpleValueType(parameter.getParameterType())
                && parameter.hasParameterAnnotation(FormModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Assert.state(mavContainer != null, "FormModelResolver requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "FormModelResolver requires WebDataBinderFactory");

        FormModelInfo formModelInfo = getFormModelInfo(parameter);
        Object instance = createParameterInstance(parameter, mavContainer);
        WebDataBinder binder = binderFactory.createBinder(webRequest, instance, formModelInfo.getParamName());
        binder.setFieldDefaultPrefix(formModelInfo.getParamPrefix());
        if (binder instanceof WebRequestDataBinder) {

            ((WebRequestDataBinder) binder).bind(webRequest);
        } else {

            ServletRequest request = webRequest.getNativeRequest(ServletRequest.class);
            if (request != null) {
                if (binder instanceof ExtendedServletRequestDataBinder) {
                    ((ExtendedServletRequestDataBinder) binder).bind(request);
                } else {
                    binder.bind(new ServletRequestParameterPropertyValues(request, formModelInfo.getParamPrefix()));
                }
            }
        }
        return instance;
    }

    private FormModelInfo getFormModelInfo(MethodParameter mp) {

        return formModelInfoCache.computeIfAbsent(mp, parameter -> {

            FormModel formModel = parameter.getParameterAnnotation(FormModel.class);
            Assert.state(formModel != null, "Not found FormModel annotation");
            String name = parameter.getParameterName();
            Assert.state(name != null, "ParameterName is null");
            String prefix = calculateParamPrefix(formModel, name);
            return new FormModelInfo(prefix, name);
        });
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

    protected static class FormModelInfo {

        private final String paramPrefix;
        private final String paramName;

        public FormModelInfo(String paramPrefix, String paramName) {
            this.paramPrefix = paramPrefix;
            this.paramName = paramName;
        }

        public String getParamPrefix() {
            return paramPrefix;
        }

        public String getParamName() {
            return paramName;
        }

        @Override
        public String toString() {
            return "FormModelInfo{" +
                    "paramPrefix='" + paramPrefix + '\'' +
                    ", paramName='" + paramName + '\'' +
                    '}';
        }
    }
}
