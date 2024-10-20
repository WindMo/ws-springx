package ws.spring.jdbc.dynamic.annotation;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AopInvocationException;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ConcurrentReferenceHashMap;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-04-04.
 */
public class DataSourceSwitchInterceptor implements MethodInterceptor {

    private final Map<Method, String> dataSourceNameCache = new ConcurrentReferenceHashMap<>();
    private final SwitchableDataSourceSelector selector;

    public DataSourceSwitchInterceptor(SwitchableDataSourceSelector selector) {
        this.selector = Objects.requireNonNull(selector);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Method method = invocation.getMethod();
        Class<?> targetClass = AopUtils.getTargetClass(invocation.getThis());
        String dataSourceName = findDataSourceName(method, targetClass);
        if (dataSourceName == null) {
            return invocation.proceed();
        } else {
            try {
                return selector.runWith(dataSourceName, () -> {
                    try {
                        return invocation.proceed();
                    } catch (Throwable e) {
                        throw new AopInvocationException("unused", e);
                    }
                });
            } catch (AopInvocationException e) {
                throw e.getCause();
            }
        }
    }

    @Nullable
    private String findDataSourceName(Method method, Class<?> targetClass) {

        String dataSourceName = dataSourceNameCache.computeIfAbsent(method, md -> {

            DataSource annotation = AnnotatedElementUtils.getMergedAnnotation(method, DataSource.class);
            if (annotation == null) {
                annotation = AnnotatedElementUtils.getMergedAnnotation(targetClass, DataSource.class);
            }
            return annotation == null ? "" : annotation.value();
        });
        return dataSourceName.isEmpty() ? null : dataSourceName;
    }
}