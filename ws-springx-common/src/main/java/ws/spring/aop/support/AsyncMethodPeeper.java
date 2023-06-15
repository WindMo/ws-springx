package ws.spring.aop.support;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.aop.MethodPeeper;
import ws.spring.aop.ReturnValuePeeper;

import java.util.concurrent.Executor;

/**
 * @author WindShadow
 * @version 2022-02-12.
 */

public abstract class AsyncMethodPeeper<T> extends AbstractMethodPeeper<T> {

    private Executor executor;

    public AsyncMethodPeeper() {
    }

    public AsyncMethodPeeper(GlobalMethodPeekHandler globalMethodPeekHandler) {
        super(globalMethodPeekHandler);
    }

    public AsyncMethodPeeper(Executor executor) {
        this.executor = executor;
    }

    public AsyncMethodPeeper(GlobalMethodPeekHandler globalMethodPeekHandler, Executor executor) {
        super(globalMethodPeekHandler);
        this.executor = executor;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.state(this.executor != null, "The executor is not set");
    }

    @Nullable
    protected MethodPeeper<T> getAsyncMethodPeeper(@Nullable MethodPeeper<T> methodPeeper) {

        return methodPeeper == null ?
                null : (exposurePoint, clazz, method, args) -> getAsyncMethodReturnValuePeeper(methodPeeper.peekArguments(exposurePoint, clazz, method, args));
    }

    @Nullable
    protected ReturnValuePeeper<T> getAsyncMethodReturnValuePeeper(@Nullable ReturnValuePeeper<T> returnValuePeeper) {

        return returnValuePeeper == null ?
                null : (returnValue, ex) -> this.executor.execute(() -> returnValuePeeper.peekReturnValue(returnValue, ex));
    }
}
