package ws.spring.util;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author WindShadow
 * @version 2022-11-06.
 */

public class GenericReflectionUtils extends ReflectionUtils {

    private static final Field MODIFIERS;

    static {

        MODIFIERS = findField(Field.class, "modifiers", int.class);
        Assert.state(MODIFIERS != null, "The modifiers field of Field.class cannot be retrieved");
        MODIFIERS.setAccessible(true);
    }

    public static <T> void setFinalFieldValue(T value, Field field, Object target) {


        setField(MODIFIERS, field, field.getModifiers() & ~Modifier.FINAL);
        setField(field, target, value);
    }
}
