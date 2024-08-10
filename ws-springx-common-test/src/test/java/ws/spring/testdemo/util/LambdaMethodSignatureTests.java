package ws.spring.testdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import ws.spring.util.lambda.IllegalMethodSignatureException;

import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2024-10-12.
 */
@Slf4j
public class LambdaMethodSignatureTests {

    private static final Class<?> MAIN_CLASS;
    private static final String SIGN_TYPE_METHOD = "signType";
    private static final String TO_SIGNATURE_METHOD = "toSignature";

    static {
        try {
            MAIN_CLASS = ClassUtils.forName("ws.spring.util.lambda.LambdaMethodSignature", null);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void parsePrimitiveTypeSignatureClassTest() {

        String method = "parsePrimitiveTypeSignatureClass";
        Assertions.assertSame(byte.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'B'));
        Assertions.assertSame(int.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'I'));
        Assertions.assertSame(short.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'S'));
        Assertions.assertSame(long.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'J'));
        Assertions.assertSame(float.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'F'));
        Assertions.assertSame(double.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'D'));
        Assertions.assertSame(boolean.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'Z'));
        Assertions.assertSame(char.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'C'));
        Assertions.assertSame(void.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'V'));
        Exception e;
        e = Assertions.assertThrows(IllegalMethodSignatureException.class, () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, 'P'));
        log.info("IllegalMethodSignature: {}", e.getMessage());
    }

    @Test
    void parseTypeSignatureClassTest() {

        String method = "parseTypeSignatureClass";
        Consumer<Class<?>> test =
                cla -> Assertions.assertSame(cla, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, invokeSignType(cla)));

        test.accept(String.class);
        test.accept(Object.class);
        test.accept(Integer.class);
        test.accept(Class.class);

        Assertions.assertSame(byte.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "B"));
        Assertions.assertSame(int.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "I"));
        Assertions.assertSame(short.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "S"));
        Assertions.assertSame(long.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "J"));
        Assertions.assertSame(float.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "F"));
        Assertions.assertSame(double.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "D"));
        Assertions.assertSame(boolean.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "Z"));
        Assertions.assertSame(char.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "C"));
        Assertions.assertSame(void.class, ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "V"));

        Exception e;
        e = Assertions.assertThrows(IllegalMethodSignatureException.class, () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "P"));
        log.info("IllegalMethodSignature: {}", e.getMessage());
        e = Assertions.assertThrows(IllegalMethodSignatureException.class, () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "abc"));
        log.info("IllegalMethodSignature: {}", e.getMessage());
        e = Assertions.assertThrows(IllegalMethodSignatureException.class, () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, invokeSignType(String.class) + ";"));
        log.info("IllegalStateException: {}", e.getMessage());
    }

    @Test
    void parseMethodSignatureTest() {

        String method = "parseMethodSignature";
        String methodSignature;
        Object result;

        methodSignature = String.format("(%s%s)%s", invokeSignType(int.class), invokeSignType(boolean.class), invokeSignType(String.class));
        result = ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, methodSignature);
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new Class<?>[]{int.class, boolean.class}, ReflectionTestUtils.invokeMethod(result, "getParameterTypes"));
        Assertions.assertSame(String.class, ReflectionTestUtils.invokeMethod(result, "getReturnType"));
        Assertions.assertEquals(invokeToSignature(result), methodSignature);

        methodSignature = "()V";
        result = ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, methodSignature);
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new Class<?>[0], ReflectionTestUtils.invokeMethod(result, "getParameterTypes"));
        Assertions.assertSame(void.class, ReflectionTestUtils.invokeMethod(result, "getReturnType"));
        Assertions.assertEquals(invokeToSignature(result), methodSignature);

        methodSignature = String.format("(%s)%s", invokeSignType(String.class), invokeSignType(void.class));
        result = ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, methodSignature);
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new Class<?>[]{String.class}, ReflectionTestUtils.invokeMethod(result, "getParameterTypes"));
        Assertions.assertSame(void.class, ReflectionTestUtils.invokeMethod(result, "getReturnType"));
        Assertions.assertEquals(invokeToSignature(result), methodSignature);

        methodSignature = String.format("(%s%s)%s", invokeSignType(int.class), invokeSignType(String.class), invokeSignType(int.class));
        result = ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, methodSignature);
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new Class<?>[]{int.class, String.class}, ReflectionTestUtils.invokeMethod(result, "getParameterTypes"));
        Assertions.assertSame(int.class, ReflectionTestUtils.invokeMethod(result, "getReturnType"));
        Assertions.assertEquals(invokeToSignature(result), methodSignature);

        methodSignature = String.format("(%s%s)%s", invokeSignType(int.class), invokeSignType(Integer.class), invokeSignType(Integer.class));
        result = ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, methodSignature);
        Assertions.assertNotNull(result);
        Assertions.assertArrayEquals(new Class<?>[]{int.class, Integer.class}, ReflectionTestUtils.invokeMethod(result, "getParameterTypes"));
        Assertions.assertSame(Integer.class, ReflectionTestUtils.invokeMethod(result, "getReturnType"));
        Assertions.assertEquals(invokeToSignature(result), methodSignature);

        Exception e;
        e = Assertions.assertThrows(IllegalMethodSignatureException.class , () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "(V)V"));
        log.info("IllegalMethodSignature: {}", e.getMessage());

        e = Assertions.assertThrows(IllegalMethodSignatureException.class , () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "(Ljava/lang/Integer)Ljava/lang/Integer;"));
        log.info("IllegalMethodSignature: {}", e.getMessage());

        e = Assertions.assertThrows(IllegalMethodSignatureException.class , () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "(L;)Ljava/lang/Integer;"));
        log.info("IllegalMethodSignature: {}", e.getMessage());

        e = Assertions.assertThrows(IllegalMethodSignatureException.class , () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "()V;"));
        log.info("IllegalMethodSignature: {}", e.getMessage());

        e = Assertions.assertThrows(IllegalMethodSignatureException.class , () -> ReflectionTestUtils.invokeMethod(MAIN_CLASS, method, "()"));
        log.info("IllegalMethodSignature: {}", e.getMessage());
    }

    @Test
    void instanceTest() {

        Class<?>[] parameterTypes = new Class<?>[]{int.class, String.class};
        Class<?> returnType = String.class;
        Object instance = ReflectUtils.newInstance(MAIN_CLASS, new Class<?>[]{parameterTypes.getClass(), Class.class}, new Object[]{parameterTypes, returnType});
        String methodSignature = invokeToSignature(instance);
        Assertions.assertEquals(instance, ReflectionTestUtils.invokeMethod(MAIN_CLASS, "parseMethodSignature", methodSignature));
    }

    private static <T> String invokeSignType(Class<T> cla) {
        return ReflectionTestUtils.invokeMethod(MAIN_CLASS, SIGN_TYPE_METHOD, cla);
    }

    private static String invokeToSignature(Object obj) {

        Assert.isTrue(MAIN_CLASS.isInstance(obj), String.format("The obj is not instance of <%s>", MAIN_CLASS.getName()));
        return ReflectionTestUtils.invokeMethod(obj, TO_SIGNATURE_METHOD);
    }
}
