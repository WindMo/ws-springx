package ws.spring.testdemo.aop.support;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.support.AnnotationMethodPeeper;
import ws.spring.aop.support.GlobalMethodPeekHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.BiPredicate;

/**
 * @author WindShadow
 * @version 2022-01-27.
 * @see AnnotationMethodPeeper
 */

public class AnnotationMethodPeeperTests {

    private GlobalMethodPeekHandler globalMethodPeekHandler;
    private Executor executor;
    private AnnotationMethodPeeper<?> methodPeeper;

    @BeforeEach
    void initMethodPeeper() {

        globalMethodPeekHandler = Mockito.spy(GlobalMethodPeekHandler.class);
        executor = Mockito.spy(Executor.class);
        methodPeeper = new AnnotationMethodPeeper<>();
        methodPeeper.setGlobalLogAdvice(globalMethodPeekHandler);
        methodPeeper.setExecutor(executor);
    }

    @Test
    void validateMethodReturnTypeTest() throws NoSuchMethodException {

        Method validateMethod = AnnotationMethodPeeper.class.getDeclaredMethod("isReasonableMethodReturnType", Method.class, Method.class);
        validateMethod.setAccessible(true);
        BiPredicate<String, String> validator = (peekMethodName, exposureMethodName) -> {

            Method exposureMethod = ClassUtils.getMethod(ExposurerClass.class, exposureMethodName);
            Method peekMethod = ClassUtils.getMethod(PeeperClass.class, peekMethodName);

            return (boolean) ReflectionUtils.invokeMethod(validateMethod, methodPeeper, peekMethod, exposureMethod);
        };

        // 正常测试
        Assertions.assertTrue(() -> validator.test("voidMethod", "voidMethod"));
        Assertions.assertTrue(() -> validator.test("returnVoidMethod", "returnVoidMethod"));
        Assertions.assertTrue(() -> validator.test("returnVoidMethod", "voidMethod"));

        Assertions.assertTrue(() -> validator.test("returnIntegerMethod", "returnIntMethod"));
        Assertions.assertTrue(() -> validator.test("returnIntegerMethod", "returnIntegerMethod"));

        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnObjectMethod"));

        Assertions.assertTrue(() -> validator.test("returnStringMethod", "returnStringMethod"));
        Assertions.assertTrue(() -> validator.test("returnListStringMethod", "returnListStringMethod"));

        Assertions.assertTrue(() -> validator.test("returnCharSequenceMethod", "returnCharSequenceMethod"));
        Assertions.assertTrue(() -> validator.test("returnListCharSequenceMethod", "returnListCharSequenceMethod"));
        // 泛型 T 识别为 ? 通配符，故可以匹配
        Assertions.assertTrue(() -> validator.test("returnGenericityMethod", "returnGenericityMethod"));

        // 测试不观测返回值
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnVoidMethod"));
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnIntMethod"));
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnIntegerMethod"));
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnObjectMethod"));
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnStringMethod"));
        Assertions.assertTrue(() -> validator.test("voidMethod", "returnListStringMethod"));

        // 兼容测试
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnVoidMethod"));
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnIntMethod"));
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnIntegerMethod"));
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnObjectMethod"));
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnStringMethod"));
        Assertions.assertTrue(() -> validator.test("returnObjectMethod", "returnListStringMethod"));

        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnVoidMethod"));
        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnIntMethod"));
        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnIntegerMethod"));
        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnObjectMethod"));
        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnStringMethod"));
        Assertions.assertTrue(() -> validator.test("returnWildcardMethod", "returnListStringMethod"));


        Assertions.assertTrue(() -> validator.test("returnCharSequenceMethod", "returnStringMethod"));
        Assertions.assertTrue(() -> validator.test("returnListWildcardMethod", "returnListStringMethod"));
        Assertions.assertTrue(() -> validator.test("returnListWildcardMethod", "returnListCharSequenceMethod"));

        // 匹配失败测试
        Assertions.assertFalse(() -> validator.test("returnListObjectMethod", "returnListStringMethod"));
        Assertions.assertFalse(() -> validator.test("returnListIntegerMethod", "returnListStringMethod"));
        Assertions.assertFalse(() -> validator.test("returnListCharSequenceMethod", "returnListStringMethod"));
    }

    @Test
    void validateMethodArgumentsTest() {

        // 正常测试
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptEmptyMethod").validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptIntMethod", "acceptIntMethod")
                .setPeekMethodArgClasses(int.class).setExposureMethodArgClasses(int.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptIntegerMethod", "acceptIntegerMethod")
                .setPeekMethodArgClasses(Integer.class).setExposureMethodArgClasses(Integer.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptObjectMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptStringMethod", "acceptStringMethod")
                .setPeekMethodArgClasses(String.class).setExposureMethodArgClasses(String.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptListStringMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(List.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptCharSequenceMethod", "acceptCharSequenceMethod")
                .setPeekMethodArgClasses(CharSequence.class).setExposureMethodArgClasses(CharSequence.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptListCharSequenceMethod", "acceptListCharSequenceMethod")
                .setPeekMethodArgClasses(List.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        // 泛型 T 识别为 ? 通配符，故可以匹配
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptGenericityMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));

        // 测试不观测参数
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptIntMethod")
                .setExposureMethodArgClasses(int.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptIntegerMethod")
                .setExposureMethodArgClasses(Integer.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptObjectMethod")
                .setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptStringMethod")
                .setExposureMethodArgClasses(String.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptListStringMethod")
                .setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptCharSequenceMethod")
                .setExposureMethodArgClasses(CharSequence.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptListCharSequenceMethod")
                .setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptEmptyMethod", "acceptGenericityMethod")
                .setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));

        // 兼容测试
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptIntMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(int.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptIntegerMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Integer.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptStringMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(String.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptCharSequenceMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(CharSequence.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptListCharSequenceMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptObjectMethod", "acceptGenericityMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));

        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptIntMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(int.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptIntegerMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Integer.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptObjectMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(Object.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptStringMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(String.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptCharSequenceMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(CharSequence.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptGenericityMethod", "acceptListCharSequenceMethod")
                .setPeekMethodArgClasses(Object.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));

        Assertions.assertTrue(() -> validateMethodArguments("acceptIntegerMethod", "acceptIntMethod")
                .setPeekMethodArgClasses(Integer.class).setExposureMethodArgClasses(int.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertTrue(() -> validateMethodArguments("acceptIntMethod", "acceptIntegerMethod")
                .setPeekMethodArgClasses(int.class).setExposureMethodArgClasses(Integer.class)
                .validateMethodArguments(methodPeeper));

        Assertions.assertTrue(() -> validateMethodArguments("acceptCharSequenceMethod", "acceptStringMethod")
                .setPeekMethodArgClasses(CharSequence.class).setExposureMethodArgClasses(String.class)
                .validateMethodArguments(methodPeeper));

        // 匹配失败测试
        Assertions.assertFalse(() -> validateMethodArguments("acceptStringMethod", "acceptCharSequenceMethod")
                .setPeekMethodArgClasses(String.class).setExposureMethodArgClasses(CharSequence.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertFalse(() -> validateMethodArguments("acceptListIntegerMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(List.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertFalse(() -> validateMethodArguments("acceptListCharSequenceMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(List.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));
        Assertions.assertFalse(() -> validateMethodArguments("acceptListObjectMethod", "acceptListStringMethod")
                .setPeekMethodArgClasses(List.class).setExposureMethodArgClasses(List.class)
                .validateMethodArguments(methodPeeper));

    }

    private FindMethodHelper validateMethodArguments(String peekMethodName, String exposureMethodName) {

        return new FindMethodHelper().setPeekMethodName(peekMethodName).setExposureMethodName(exposureMethodName);
    }

    static class ExposurerClass {

        public void voidMethod() {
        }

        public Void returnVoidMethod() {
            return null;
        }

        public int returnIntMethod() {
            return 0;
        }

        public Integer returnIntegerMethod() {
            return null;
        }

        public Object returnObjectMethod() {
            return null;
        }

        public String returnStringMethod() {
            return null;
        }

        public List<String> returnListStringMethod() {
            return null;
        }

        public CharSequence returnCharSequenceMethod() {
            return null;
        }

        public List<CharSequence> returnListCharSequenceMethod() {
            return null;
        }

        public <T> T returnGenericityMethod() {
            return null;
        }


        public void acceptEmptyMethod() {
        }

        public void acceptIntMethod(int a) {
        }

        public void acceptIntegerMethod(Integer a) {
        }

        public void acceptObjectMethod(Object o) {
        }

        public void acceptStringMethod(String s) {
        }

        public void acceptListStringMethod(List<String> list) {
        }

        public void acceptCharSequenceMethod(CharSequence charSequence) {
        }

        public void acceptListCharSequenceMethod(List<CharSequence> charSequences) {
        }

        public <T> void acceptGenericityMethod(T obj) {
        }
    }

    static class PeeperClass {

        public void voidMethod() {
        }

        public ReturnValuePeeper<Void> returnVoidMethod() {
            return null;
        }

        public ReturnValuePeeper<Integer> returnIntegerMethod() {
            return null;
        }

        public ReturnValuePeeper<Object> returnObjectMethod() {
            return null;
        }

        public ReturnValuePeeper<String> returnStringMethod() {
            return null;
        }

        public ReturnValuePeeper<List<String>> returnListStringMethod() {
            return null;
        }

        public ReturnValuePeeper<CharSequence> returnCharSequenceMethod() {
            return null;
        }

        public ReturnValuePeeper<List<CharSequence>> returnListCharSequenceMethod() {
            return null;
        }

        public <T> ReturnValuePeeper<T> returnGenericityMethod() {
            return null;
        }


        public ReturnValuePeeper<List<Object>> returnListObjectMethod() {
            return null;
        }

        public ReturnValuePeeper<List<Integer>> returnListIntegerMethod() {
            return null;
        }

        public ReturnValuePeeper<?> returnWildcardMethod() {
            return null;
        }

        public ReturnValuePeeper<List<?>> returnListWildcardMethod() {
            return null;
        }


        public void acceptEmptyMethod() {
        }

        public void acceptIntMethod(int a) {
        }

        public void acceptIntegerMethod(Integer a) {
        }

        public void acceptObjectMethod(Object o) {
        }

        public void acceptStringMethod(String s) {
        }

        public void acceptListStringMethod(List<String> list) {
        }

        public void acceptCharSequenceMethod(CharSequence charSequence) {
        }

        public void acceptListCharSequenceMethod(List<CharSequence> charSequences) {
        }

        public <T> void acceptGenericityMethod(T obj) {
        }


        public void acceptListObjectMethod(List<Object> objectList) {
        }

        public void acceptListIntegerMethod(List<Integer> integerList) {
        }

        public void acceptListWildcardMethod(List<?> list) {
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private static class FindMethodHelper {

        private String peekMethodName;
        private String exposureMethodName;
        private Class<?>[] peekMethodArgClasses = new Class[]{};
        private Class<?>[] exposureMethodArgClasses = new Class[]{};

        public boolean validateMethodArguments(AnnotationMethodPeeper<?> methodPeeper) {

            try {
                Method validateMethod = AnnotationMethodPeeper.class.getDeclaredMethod("isReasonableMethodArguments", Method.class, Method.class);
                validateMethod.setAccessible(true);
                Method exposureMethod = ClassUtils.getMethod(ExposurerClass.class, exposureMethodName, exposureMethodArgClasses);
                Method peekMethod = ClassUtils.getMethod(PeeperClass.class, peekMethodName, peekMethodArgClasses);

                return (boolean) ReflectionUtils.invokeMethod(validateMethod, methodPeeper, peekMethod, exposureMethod);
            } catch (NoSuchMethodException e) {
                // ignore
                throw new UnsupportedOperationException(e);
            }
        }

        public FindMethodHelper setPeekMethodName(String peekMethodName) {
            this.peekMethodName = peekMethodName;
            return this;
        }

        public FindMethodHelper setExposureMethodName(String exposureMethodName) {
            this.exposureMethodName = exposureMethodName;
            return this;
        }

        public FindMethodHelper setPeekMethodArgClasses(Class<?>... peekMethodArgClasses) {
            this.peekMethodArgClasses = peekMethodArgClasses;
            return this;
        }

        public FindMethodHelper setExposureMethodArgClasses(Class<?>... exposureMethodArgClasses) {
            this.exposureMethodArgClasses = exposureMethodArgClasses;
            return this;
        }
    }
}
