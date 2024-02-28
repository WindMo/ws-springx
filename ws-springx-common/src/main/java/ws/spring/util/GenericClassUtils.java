package ws.spring.util;

import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author WindShadow
 * @version 2022-01-26.
 */

public abstract class GenericClassUtils extends ClassUtils {

    public static String getMethodPrimaryName(Method method) {

        return getMethodPrimaryName(method, null);
    }

    public static String getMethodPrimaryName(Method method, Class<?> clazz) {

        String qualifiedMethodName = getQualifiedMethodName(method, clazz);
        Class<?>[] parameterTypes = method.getParameterTypes();
        return Arrays.stream(parameterTypes)
                .map(Class::getName)
                .collect(Collectors.joining(",", qualifiedMethodName + "(", ")"));
    }
}
