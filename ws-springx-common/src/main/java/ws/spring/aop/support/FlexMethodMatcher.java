package ws.spring.aop.support;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.StaticMethodMatcher;

import java.lang.reflect.Method;
import java.util.Objects;

public class FlexMethodMatcher extends StaticMethodMatcher {

    private final ClassFilter classFilter;
    private final MethodMatcher methodMatcher;

    FlexMethodMatcher(ClassFilter classFilter, MethodMatcher methodMatcher) {
        this.classFilter = Objects.requireNonNull(classFilter);
        this.methodMatcher = Objects.requireNonNull(methodMatcher);
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {

        if (methodMatcher.matches(method, targetClass)) return true;
        return classFilter.matches(targetClass);
    }
}