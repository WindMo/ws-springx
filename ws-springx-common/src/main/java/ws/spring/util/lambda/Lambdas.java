package ws.spring.util.lambda;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandleInfo;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author WindShadow
 * @version 2024-08-08.
 */
public class Lambdas {

    private static final Map<SerializableLambda, SerializedLambda> SERIALIZED_LAMBDA_CACHE = new ConcurrentReferenceHashMap<>();

    @Nullable
    public static Field findFieldForReferencedMethod(SC<?> sc) {
        return findFieldForReferencedMethod((SerializableLambda) sc);
    }

    @Nullable
    public static Field findFieldForReferencedMethod(SF<?, ?> sf) {
        return findFieldForReferencedMethod((SerializableLambda) sf);
    }

    @Nullable
    public static Field findFieldForReferencedMethod(SS<?> ss) {
        return findFieldForReferencedMethod((SerializableLambda) ss);
    }

    @Nullable
    private static Field findFieldForReferencedMethod(SerializableLambda lambdaObj) {

        return Optional.ofNullable(findPropertyHolderForReferencedMethod(lambdaObj))
                .map(holder -> {

                    PropertyDescriptor pd = holder.getPropertyDescriptor();
                    return ReflectionUtils.findField(holder.getBeanClass(), pd.getName(), pd.getPropertyType());
                })
                .orElse(null);
    }

    @Nullable
    public static PropertyDescriptor findPropertyForReferencedMethod(SC<?> sc) {
        return findPropertyForReferencedMethod((SerializableLambda) sc);
    }

    @Nullable
    public static PropertyDescriptor findPropertyForReferencedMethod(SF<?, ?> sf) {
        return findPropertyForReferencedMethod((SerializableLambda) sf);
    }

    @Nullable
    public static PropertyDescriptor findPropertyForReferencedMethod(SS<?> ss) {
        return findPropertyForReferencedMethod((SerializableLambda) ss);
    }

    @Nullable
    private static PropertyDescriptor findPropertyForReferencedMethod(SerializableLambda lambdaObj) {
        return Optional.ofNullable(findPropertyHolderForReferencedMethod(lambdaObj))
                .map(PropertyDescriptorHolder::getPropertyDescriptor)
                .orElse(null);
    }

    @Nullable
    private static PropertyDescriptorHolder findPropertyHolderForReferencedMethod(SerializableLambda lambdaObj) {

        Method method = getReferencedMethod(lambdaObj);
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method);
            return propertyDescriptor == null ? null : new PropertyDescriptorHolder(method.getDeclaringClass(), propertyDescriptor);
        } catch (IntrospectionException e) {
            throw new IllegalStateException("Find property descriptor for ReferencedMethod failed", e);
        }
    }

    public static Method getReferencedMethod(SC<?> sc) {
        return getReferencedMethod((SerializableLambda) sc);
    }

    public static Method getReferencedMethod(SF<?, ?> sf) {
        return getReferencedMethod((SerializableLambda) sf);
    }

    public static Method getReferencedMethod(SP<?> sp) {
        return getReferencedMethod((SerializableLambda) sp);
    }

    public static Method getReferencedMethod(SR sr) {
        return getReferencedMethod((SerializableLambda) sr);
    }

    public static Method getReferencedMethod(SS<?> ss) {
        return getReferencedMethod((SerializableLambda) ss);
    }

    private static Method getReferencedMethod(SerializableLambda lambdaObj) {

        SerializedLambda lambda = serialize(lambdaObj);
        int methodKind = lambda.getImplMethodKind();
        Assert.isTrue(MethodHandleInfo.REF_invokeVirtual == methodKind || MethodHandleInfo.REF_invokeStatic == methodKind,
                "The lambda object is not a method reference");

        String implClass = lambda.getImplClass();
        String className = implClass.replace("/", ".");
        Class<?> cla;
        try {
            cla = ClassUtils.forName(className, null);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(String.format("Not found lambda implemented class[%s]", implClass), e);
        }
        String methodName = lambda.getImplMethodName();
        String signature = lambda.getImplMethodSignature();
        LambdaMethodSignature metadata = LambdaMethodSignature.parseMethodSignature(signature);
        return ReflectionUtils.findMethod(cla, methodName, metadata.getParameterTypes());
    }

    public static String getImplMethodName(SC<?> sc) {
        return serialize(sc).getImplMethodName();
    }

    public static String getImplMethodName(SF<?, ?> sf) {
        return serialize(sf).getImplMethodName();
    }

    public static String getImplMethodName(SP<?> sp) {
        return serialize(sp).getImplMethodName();
    }

    public static String getImplMethodName(SR sr) {
        return serialize(sr).getImplMethodName();
    }

    public static String getImplMethodName(SS<?> ss) {
        return serialize(ss).getImplMethodName();
    }

    public static SerializedLambda serialize(SC<?> sc) {
        return serialize((SerializableLambda) sc);
    }

    public static SerializedLambda serialize(SF<?, ?> sf) {
        return serialize((SerializableLambda) sf);
    }

    public static SerializedLambda serialize(SP<?> sp) {
        return serialize((SerializableLambda) sp);
    }

    public static SerializedLambda serialize(SR sr) {
        return serialize((SerializableLambda) sr);
    }

    public static SerializedLambda serialize(SS<?> ss) {
        return serialize((SerializableLambda) ss);
    }

    private static SerializedLambda serialize(SerializableLambda lambdaObj) {

        return SERIALIZED_LAMBDA_CACHE.computeIfAbsent(lambdaObj, lambda -> {

            Class<?> clazz = Objects.requireNonNull(lambda).getClass();
            Assert.isTrue(clazz.isSynthetic(), "Support only synthetic lambda object");
            try {
                Method writeReplace = clazz.getDeclaredMethod("writeReplace");
                ReflectionUtils.makeAccessible(writeReplace);
                return (SerializedLambda) writeReplace.invoke(lambda);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException("The object is not a lambda object");
            } catch (Exception e) {
                throw new IllegalStateException("Invoke 'writeReplace' method of failed", e);
            }
        });
    }

    private static class PropertyDescriptorHolder {

        private final Class<?> beanClass;
        private final PropertyDescriptor propertyDescriptor;

        public PropertyDescriptorHolder(Class<?> beanClass, PropertyDescriptor original) throws IntrospectionException {
            this.beanClass = Objects.requireNonNull(beanClass);
            this.propertyDescriptor = Objects.requireNonNull(original);
        }

        public Class<?> getBeanClass() {
            return beanClass;
        }

        public PropertyDescriptor getPropertyDescriptor() {
            return propertyDescriptor;
        }
    }
}

