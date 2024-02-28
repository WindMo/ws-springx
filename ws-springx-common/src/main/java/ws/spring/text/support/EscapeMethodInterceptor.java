package ws.spring.text.support;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.text.Escaper;
import ws.spring.text.annotaion.Escape;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WindShadow
 * @version 2023-05-01.
 */

class EscapeMethodInterceptor implements MethodInterceptor {

    private final Map<Class<? extends Escaper>, Escaper> escaperInstanceCache = new ConcurrentHashMap<>();
    private final Map<Method, Map<Integer, Escaper>> escaperMappingCache = new ConcurrentHashMap<>();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object[] args = invocation.getArguments();
        Method method = invocation.getMethod();
        Map<Integer, Escaper> escapeParamMapping = escaperMappingCache.computeIfAbsent(method, this::parse);
        if (escapeParamMapping != null) {
            escapeParamMapping.forEach((index, escaper) -> args[index] = doEscape(index, args[index], escaper));
        }
        return invocation.proceed();
    }

    @Nullable
    private Map<Integer, Escaper> parse(Method method) {

        Map<Integer, Escaper> mapping = null;
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < method.getParameterCount(); i++) {

            Escape escape = AnnotatedElementUtils.getMergedAnnotation(parameters[i], Escape.class);
            if (escape != null) {

                Class<? extends Escaper> escaperClass = escape.value();
                Escaper escaper = escaperInstanceCache.computeIfAbsent(escaperClass, BeanUtils::instantiateClass);
                if (mapping == null) {
                    mapping = new HashMap<>();
                }
                mapping.put(i, escaper);
            }
        }
        return mapping;
    }

    @Nullable
    private Object doEscape(int argIndex, @Nullable Object arg, Escaper escaper) {

        if (arg == null) return null;
        Assert.state(arg instanceof String, "Cannot escape arguments[" + argIndex + "] that are not String");
        return escaper.escape((String) arg);
    }
}
