package ws.spring.testdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import ws.spring.util.lambda.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-08-06.
 */
@Slf4j
public class LambdasTests {

    @Test
    void getImplMethodTest() {

        Assertions.assertEquals("println", Lambdas.getImplMethodName((SC<String>) System.out::println));
        Assertions.assertEquals("valueOf", Lambdas.getImplMethodName((SF<Object, String>) String::valueOf));
        Assertions.assertEquals("isEmpty", Lambdas.getImplMethodName((SP<String>) StringUtils::isEmpty));
        Assertions.assertEquals("getImplMethodTest", Lambdas.getImplMethodName((SR) this::getImplMethodTest));
        Assertions.assertEquals("toString", Lambdas.getImplMethodName((SR) LambdasTests.class::toString));
        Assertions.assertEquals("toString", Lambdas.getImplMethodName((SS<String>) LambdasTests.class::toString));

        // 这里不是asList的方法引用，实际会编译成书写lambda表达式的方式以忽略返回值，因为asList方法与SC的accept不是契合的
        Assertions.assertNotEquals("asList", Lambdas.getImplMethodName((SC<String>) Arrays::asList));

        SC<String> sc = new SC<String>() {
            @Override
            public void accept(String s) {
            }
        };

        Exception ex = Assertions.assertThrows(IllegalArgumentException.class, () -> Lambdas.getImplMethodName(sc));
        log.info("IllegalArgumentException: {}", ex.getMessage());
    }

    @Test
    void getMethodReferenceTest() {

        Assertions.assertEquals(
                ClassUtils.getMethod(PrintStream.class, "println", String.class),
                Lambdas.getReferencedMethod((SC<String>) System.out::println));
        Assertions.assertEquals(
                ClassUtils.getMethod(String.class, "valueOf", Object.class),
                Lambdas.getReferencedMethod((SF<Object, String>) String::valueOf));
        Assertions.assertEquals(
                ClassUtils.getMethod(StringUtils.class, "isEmpty", Object.class),
                Lambdas.getReferencedMethod((SP<String>) StringUtils::isEmpty));
        Assertions.assertEquals(
                ReflectionUtils.findMethod(LambdasTests.class, "getImplMethodTest"),
                Lambdas.getReferencedMethod((SR) this::getImplMethodTest));
        Assertions.assertEquals(
                ClassUtils.getMethod(Class.class, "toString"),
                Lambdas.getReferencedMethod((SR) LambdasTests.class::toString));
        Assertions.assertEquals(
                ClassUtils.getMethod(Class.class, "toString"),
                Lambdas.getReferencedMethod((SS<String>) LambdasTests.class::toString));

        // 这里不是asList的方法引用
        Assertions.assertNotEquals(
                ClassUtils.getMethod(Arrays.class, "asList", Object[].class),
                Lambdas.getReferencedMethod((SC<String>) Arrays::asList));
    }

    @Test
    void findPropertyForReferencedMethodTest() throws IntrospectionException {

        FakeUser fu = new FakeUser();
        PropertyDescriptor expectedProperty;

        expectedProperty = new PropertyDescriptor("name", FakeUser.class);
        Assertions.assertTrue(equalsProperty(expectedProperty, Lambdas.findPropertyForReferencedMethod((SF<FakeUser, String>) FakeUser::getName)));
        Assertions.assertTrue(equalsProperty(expectedProperty, Lambdas.findPropertyForReferencedMethod((SC<String>) fu::setName)));

        expectedProperty = new PropertyDescriptor("age", FakeUser.class, "getAge", null);
        Assertions.assertTrue(equalsProperty(expectedProperty, Lambdas.findPropertyForReferencedMethod((SF<FakeUser, Integer>) FakeUser::getAge)));

        expectedProperty = new PropertyDescriptor("email", FakeUser.class, null, "setEmail");
        Assertions.assertTrue(equalsProperty(expectedProperty, Lambdas.findPropertyForReferencedMethod((SC<String>) fu::setEmail)));

        expectedProperty = new PropertyDescriptor("fake", FakeUser.class, "isFake", null);
        Assertions.assertTrue(equalsProperty(expectedProperty, Lambdas.findPropertyForReferencedMethod(fu::isFake)));

        Assertions.assertNull(Lambdas.findFieldForReferencedMethod((SF<FakeUser, String>) FakeUser::fetchAddress));
    }

    @Test
    void findFieldForReferencedMethodTest() {

        FakeUser fu = new FakeUser();
        Field expectedField;

        expectedField = ReflectionUtils.findField(FakeUser.class, "name");
        Assertions.assertEquals(expectedField, Lambdas.findFieldForReferencedMethod((SF<FakeUser, String>) FakeUser::getName));
        Assertions.assertEquals(expectedField, Lambdas.findFieldForReferencedMethod((SC<String>) fu::setName));

        expectedField = ReflectionUtils.findField(FakeUser.class, "enabled");
        Assertions.assertEquals(expectedField, Lambdas.findFieldForReferencedMethod((SF<FakeUser, Boolean>) FakeUser::isEnabled));
        Assertions.assertEquals(expectedField, Lambdas.findFieldForReferencedMethod((SC<Boolean>) fu::setEnabled));
        Assertions.assertEquals(expectedField, Lambdas.findFieldForReferencedMethod((SS<Boolean>) fu::isEnabled));

        Assertions.assertNull(Lambdas.findFieldForReferencedMethod((SS<Boolean>) fu::isFake));
    }

    private static boolean equalsProperty(PropertyDescriptor pd1, PropertyDescriptor pd2) {

        return Objects.equals(pd1, pd2) || (pd1 != null && pd2 != null
                && Objects.equals(pd1.getName(), pd1.getName())
                && Objects.equals(pd1.getPropertyType(), pd2.getPropertyType())
                && Objects.equals(pd1.getWriteMethod(), pd2.getWriteMethod())
                && Objects.equals(pd1.getReadMethod(), pd2.getReadMethod())
                && Objects.equals(pd1.getPropertyEditorClass(), pd2.getPropertyEditorClass()));
    }
}
