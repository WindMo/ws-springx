package ws.spring.aop.support;

import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public class FlexAnnotationMatchingPointcut extends StaticMethodMatcherPointcut {

    private final MethodMatcher methodMatcher;

    public FlexAnnotationMatchingPointcut(Class<? extends Annotation> annotation, boolean checkInherited) {
        this.methodMatcher = new FlexAnnotationMethodMatcher(annotation, checkInherited);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return methodMatcher.matches(method, targetClass);
    }
}
