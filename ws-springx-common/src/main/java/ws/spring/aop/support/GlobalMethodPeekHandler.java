package ws.spring.aop.support;

import ws.spring.aop.MethodPeeper;
import ws.spring.aop.annotation.ExposurePoint;

import java.lang.reflect.Method;

/**
 * @author WindShadow
 * @version 2022-01-23.
 */

public interface GlobalMethodPeekHandler {

    /**
     * 暴露点方法未找到对应的观测者
     *
     * @param point               暴露点
     * @param exposurePointMethod 暴露点方法未
     * @param targetClass         暴露点方法所属class
     */
    void handleMissMethodPeeper(ExposurePoint point, Method exposurePointMethod, Class<?> targetClass);

    /**
     * 执行<code>methodPeeper</code>的{@linkplain MethodPeeper#peekArguments(ExposurePoint, Class, Method, Object...) 方法参数观测过程}时出错
     *
     * @param e 观测者观测暴露点方法参数时抛出的异常
     */
    void handlePeekArgumentsException(RuntimeException e, Object... args);

    /**
     * 执行<code>methodPeeper</code>返回的{@linkplain ws.spring.aop.ReturnValuePeeper#peekReturnValue(Object, Exception) 返回值观测者的观测过程}时出错
     *
     * @param returnValue 暴露点方法的返回值
     * @param e           返回值观测者的观测过程时抛出的异常
     */
    void handlePeekReturnException(RuntimeException e, Object returnValue);
}
