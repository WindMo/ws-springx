package ws.spring.aop.support;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ws.spring.aop.MethodPeeper;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.annotation.ExposurePoint;
import ws.spring.aop.annotation.PeekPoint;
import ws.spring.aop.annotation.Peeper;
import ws.spring.aop.exception.PeekMethodDeclarationException;
import ws.spring.aop.exception.PeekMethodInvokeException;
import ws.spring.util.GenericClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author WindShadow
 * @version 2022-01-21.
 */

public class AnnotationMethodPeeper<T> extends AsyncMethodPeeper<T> implements BeanPostProcessor {

    private static final ResolvableType RETURN_VALUE_PEEPER_TYPE = ResolvableType.forClass(ReturnValuePeeper.class);
    private final Map<String, MethodPeeperComposite<T>> peekPointsCache = new ConcurrentHashMap<>();

    public AnnotationMethodPeeper() {
    }

    public AnnotationMethodPeeper(GlobalMethodPeekHandler globalMethodPeekHandler) {
        super(globalMethodPeekHandler);
    }

    public AnnotationMethodPeeper(Executor executor) {
        super(executor);
    }

    public AnnotationMethodPeeper(GlobalMethodPeekHandler globalMethodPeekHandler, Executor executor) {
        super(globalMethodPeekHandler, executor);
    }

    private static String getExposurePointName(ExposurePoint exposurePoint, Method method, Class<?> clazz) {

        String exposurePointName = (String) AnnotationUtils.getValue(exposurePoint);
        if (!StringUtils.hasText(exposurePointName)) {
            exposurePointName = GenericClassUtils.getMethodPrimaryName(method, clazz);
        }
        return exposurePointName;
    }

    private static String getPeekPointName(Peeper peeper, PeekPoint peekPoint, Method method) {

        String peekPointName = (String) AnnotationUtils.getValue(peekPoint);
        if (!StringUtils.hasText(peekPointName)) {
            peekPointName = GenericClassUtils.getMethodPrimaryName(method, peeper.value());
        }
        return peekPointName;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        Peeper peeper = AnnotationUtils.findAnnotation(bean.getClass(), Peeper.class);
        if (peeper != null) {

            cachePeekPoint(peeper, bean);
        }
        return bean;
    }

    @Override
    protected MethodPeeper<T> findMethodPeeper(ExposurePoint exposurePoint, Class<?> clazz, Method exposurePointMethod) {

        String pointName = getExposurePointName(exposurePoint, exposurePointMethod, clazz);
        return this.peekPointsCache.get(pointName);
    }

    private void cachePeekPoint(Peeper peeper, Object peeperObj) {

        Class<?> peeperClass = AopUtils.getTargetClass(peeperObj);
        Method[] allDeclaredMethods = ReflectionUtils.getUniqueDeclaredMethods(peeperClass);
        for (Method method : allDeclaredMethods) {

            PeekPoint peekPoint = AnnotationUtils.findAnnotation(method, PeekPoint.class);
            if (peekPoint != null) {

                String peekPointName = getPeekPointName(peeper, peekPoint, method);
                MethodPeeper<T> methodPeeper = (exposurePoint, clazz, exposurePointMethod, args) -> this.invokePeepPointMethod(method, peeperObj, exposurePointMethod, args);
                MethodPeeperComposite<T> composite = peekPointsCache.computeIfAbsent(peekPointName, k -> new MethodPeeperComposite<>());
                if (peekPoint.asyn()) {
                    methodPeeper = getAsyncMethodPeeper(methodPeeper);
                }

                Order order = AnnotationUtils.findAnnotation(method, Order.class);
                if (order != null) {
                    composite.addMethodPeeper(methodPeeper, order.value());
                } else {
                    composite.addMethodPeeper(methodPeeper);
                }
            }
        }
    }

    /**
     * 校验观测点和暴露点的方法返回值类型
     *
     * @param peekPointMethod     观测点方法
     * @param exposurePointMethod 暴露点方法
     * @return 观测点不观测返回值或预期观测类型与暴露点方法返回值类型兼容时为true，反之false
     */
    private boolean isReasonableMethodReturnType(Method peekPointMethod, Method exposurePointMethod) {

        // 校验 peekPointMethod 返回值必须为 ReturnValuePeeper或无返回值，且 ReturnValuePeeper接口处理的返回值与暴露点方法返回值类型一致或兼容
        if (needPeekReturnValue(peekPointMethod)) {

            ResolvableType peekMethodReturnType = ResolvableType.forMethodReturnType(peekPointMethod);
            Class<?> returnTypeClass = peekMethodReturnType.resolve();
            if (returnTypeClass != null && RETURN_VALUE_PEEPER_TYPE.isAssignableFrom(returnTypeClass)) {

                // 观测返回值，观测点预期观测的类型与暴露点返回值类型必须兼容
                ResolvableType exposureMethodReturnType = ResolvableType.forMethodReturnType(exposurePointMethod);
                ResolvableType expReturnValueType = peekMethodReturnType.getGeneric(0);
                return expReturnValueType.isAssignableFrom(Void.class) || expReturnValueType.isAssignableFrom(exposureMethodReturnType);
            }
            return false;
        }
        // 不观测返回值
        return true;
    }

    /**
     * 校验观测点和暴露点的方法参数类型
     *
     * @param peekPointMethod     观测点方法
     * @param exposurePointMethod 暴露点方法
     * @return 观测点不观测参数或预期观测参数类型与暴露点方法参数类型兼容时为true，反之false
     */
    private boolean isReasonableMethodArguments(Method peekPointMethod, Method exposurePointMethod) {

        if (needPeekArguments(peekPointMethod)) {

            int peekPointMethodParameterCount = peekPointMethod.getParameterCount();
            int exposurePointMethodParameterCount = exposurePointMethod.getParameterCount();
            if (peekPointMethodParameterCount == exposurePointMethodParameterCount) {

                for (int i = 0; i < peekPointMethodParameterCount; i++) {

                    ResolvableType peekMethodParameterType = ResolvableType.forMethodParameter(peekPointMethod, i);
                    ResolvableType exposureMethodParameterType = ResolvableType.forMethodParameter(exposurePointMethod, i);
                    if (!peekMethodParameterType.isAssignableFrom(exposureMethodParameterType)) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        // 不观测参数
        return true;
    }

    private boolean needPeekArguments(Method peekPointMethod) {

        return peekPointMethod.getParameterCount() != 0;
    }

    private boolean needPeekReturnValue(Method peekPointMethod) {

        return !ResolvableType.forMethodReturnType(peekPointMethod).isAssignableFrom(Void.TYPE);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Nullable
    private ReturnValuePeeper<T> invokePeepPointMethod(Method peekPointMethod, Object peeperObj, Method exposurePointMethod, Object... args) {

        try {

            // 校验方法参数类型一致或兼容
            if (!isReasonableMethodArguments(peekPointMethod, exposurePointMethod)) {

                throw new PeekMethodDeclarationException("The parameter type of method [" + peekPointMethod + "] cannot match that of method [" + exposurePointMethod + "]");
            }
            // 校验方法返回值类型符合规则
            if (!isReasonableMethodReturnType(peekPointMethod, exposurePointMethod)) {
                throw new PeekMethodDeclarationException("The return value type of [" + peekPointMethod + "] is not a subclass of type <" + ReturnValuePeeper.class.getName() + "> or void, "
                        + "or when it is of type ReturnValuePeeper, the return value of the exposure point is expected to be of incompatible type");
            }

            Object returnObj;

            if (needPeekArguments(peekPointMethod)) {

                returnObj = peekPointMethod.invoke(peeperObj, args);
            } else {
                returnObj = peekPointMethod.invoke(peeperObj);
            }

            if (needPeekReturnValue(peekPointMethod)) {
                return (ReturnValuePeeper) returnObj;
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            throw new PeekMethodDeclarationException(e);
        } catch (InvocationTargetException e) {
            throw new PeekMethodInvokeException(e);
        }
    }
}