/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.bind;

import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import ws.spring.web.bind.annotation.FormModel;

import javax.servlet.ServletRequest;
import java.util.Map;
import java.util.Optional;
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
        ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
        Assert.state(servletRequest != null, "No ServletRequest");
        ServletRequestParameterPropertyValues values = new ServletRequestParameterPropertyValues(servletRequest, formModelInfo.getParamPrefix(), formModelInfo.getSeparator());

        Object instance = null;
        if (values.isEmpty()) {
            if (formModelInfo.isRequired()) {
                throw new ServletRequestBindingException("Missing argument '" + formModelInfo.getParamName() + "' for method parameter of type "
                        + parameter.getNestedParameterType().getSimpleName());
            }
        } else {
            // 使用实际的参数类型（如果是Optional嵌套）创建实例
            instance = createParameterInstance(parameter.nestedIfOptional(), mavContainer);
            WebDataBinder binder = binderFactory.createBinder(webRequest, instance, formModelInfo.getParamName());
            binder.setFieldDefaultPrefix(formModelInfo.getUseParamPrefix());
            binder.bind(values);
            WebDataBinderUtils.validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors()) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        return parameter.isOptional() ? Optional.ofNullable(instance) : instance;
    }

    private FormModelInfo getFormModelInfo(MethodParameter mp) {

        return formModelInfoCache.computeIfAbsent(mp, parameter -> {

            FormModel formModel = parameter.getParameterAnnotation(FormModel.class);
            Assert.state(formModel != null, "Not found FormModel annotation");
            String name = parameter.getParameterName();
            Assert.state(name != null, "ParameterName is null");
            String prefix = calculateParamPrefix(formModel, name);
            return new FormModelInfo(prefix, name, formModel.separator(), formModel.required());
        });
    }

    protected final String calculateParamPrefix(FormModel formModel, String originalParamName) {

        String prefix = formModel.prefix();
        return StringUtils.hasText(prefix) ? prefix : originalParamName;
    }

    protected Object createParameterInstance(MethodParameter parameter, ModelAndViewContainer mavContainer) {

        Class<?> clazz = parameter.getNestedParameterType();
        try {
            return BeanUtils.instantiateClass(clazz);
        } catch (BeanInstantiationException ex) {
            throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
        }
    }

    protected static class FormModelInfo {

        private final String paramPrefix;
        private final String paramName;

        private final String separator;

        private final boolean required;

        private final String useParamPrefix;

        public FormModelInfo(String paramPrefix, String paramName, String separator, boolean required) {
            this.paramPrefix = paramPrefix;
            this.paramName = paramName;
            this.separator = separator;
            this.required = required;
            this.useParamPrefix = paramPrefix + separator;
        }

        public String getParamPrefix() {
            return paramPrefix;
        }

        public String getParamName() {
            return paramName;
        }

        public boolean isRequired() {
            return required;
        }

        public String getSeparator() {
            return separator;
        }

        public String getUseParamPrefix() {
            return useParamPrefix;
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
