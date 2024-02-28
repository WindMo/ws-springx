package ws.spring.aop.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ws.spring.aop.annotation.ExposurePoint;

import java.lang.reflect.Method;

/**
 * @author WindShadow
 * @version 2022-01-23.
 */

public class AbortGlobalMethodPeekHandler implements GlobalMethodPeekHandler {

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void handleMissMethodPeeper(ExposurePoint point, Method exposurePointMethod, Class<?> targetClass) {

        if (log.isWarnEnabled()) {
            log.warn("MethodPeeper corresponding to pointName [" + point.name() + "]，exposurePointMethod [" + exposurePointMethod + "] in class [" + targetClass + "] was not found");
        }
    }

    @Override
    public void handlePeekArgumentsException(RuntimeException e, Object... args) {
        throw e;
    }

    @Override
    public void handlePeekReturnException(RuntimeException e, Object returnValue) {
        throw e;
    }
}
