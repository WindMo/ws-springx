/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util.lambda;

import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 此类将维护一个方法的参数类型与返回值类型信息
 *
 * @author WindShadow
 * @version 2024-08-08.
 */
class LambdaMethodSignature {

    private static final Map<Character, Class<?>> SIGNATURE_PRIMITIVE_TYPE_MAPPING;
    private static final Map<Class<?>, Character> PRIMITIVE_TYPE_SIGNATURE_MAPPING;

    private final Class<?>[] parameterTypes;
    private final Class<?> returnType;

    static {

        Map<Character, Class<?>> signatureMap = new HashMap<>();
        signatureMap.put('B', byte.class);
        signatureMap.put('I', int.class);
        signatureMap.put('S', short.class);
        signatureMap.put('J', long.class);
        signatureMap.put('F', float.class);
        signatureMap.put('D', double.class);
        signatureMap.put('Z', boolean.class);
        signatureMap.put('C', char.class);
        signatureMap.put('V', void.class);
        SIGNATURE_PRIMITIVE_TYPE_MAPPING = Collections.unmodifiableMap(signatureMap);
        PRIMITIVE_TYPE_SIGNATURE_MAPPING = Collections.unmodifiableMap(
                signatureMap.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey)));
    }

    LambdaMethodSignature(Class<?>[] parameterTypes, Class<?> returnType) {
        this.parameterTypes = Objects.requireNonNull(parameterTypes);
        this.returnType = Objects.requireNonNull(returnType);
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public final String toSignature() {

        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Class<?> parameterType : getParameterTypes()) {
            builder.append(signType(parameterType));
        }
        builder.append(")");
        builder.append(signType(getReturnType()));
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LambdaMethodSignature)) return false;
        LambdaMethodSignature that = (LambdaMethodSignature) o;
        return Arrays.equals(getParameterTypes(), that.getParameterTypes()) && Objects.equals(getReturnType(), that.getReturnType());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getReturnType());
        result = 31 * result + Arrays.hashCode(getParameterTypes());
        return result;
    }

    @Override
    public String toString() {
        return "MethodSignatureMetadata{" +
                "parameterTypes=" + Arrays.toString(getParameterTypes()) +
                ", returnType=" + getReturnType() +
                '}';
    }

    /**
     * 解析lambda表达式实现的方法的签名，以获取方法的参数类型与返回值类型
     *
     * @param methodSignature
     * @return
     */
    static LambdaMethodSignature parseMethodSignature(String methodSignature) throws IllegalMethodSignatureException {

        String message = String.format("Invalid lambda impl method signature[%s]", methodSignature);
        IllegalMethodSignatureException.toAssert(methodSignature.startsWith("("), message);
        int separatedIndex = Objects.requireNonNull(methodSignature).indexOf(')');
        IllegalMethodSignatureException.toAssert(separatedIndex != -1, message);

        // 解析方法参数类型
        String parameterTypeSignature = methodSignature.substring(1, separatedIndex);
        Class<?>[] parameterTypes;
        if (parameterTypeSignature.isEmpty()) {

            parameterTypes = new Class[0];
        } else {

            List<Class<?>> parameterTypeList = new ArrayList<>();
            char[] chars = parameterTypeSignature.toCharArray();
            StringBuilder refType = new StringBuilder();
            for (char c : chars) {

                if (refType.length() == 0) {

                    if (c == 'L') {
                        refType.append(c);
                    } else {
                        Class<?> type = parsePrimitiveTypeSignatureClass(c);
                        IllegalMethodSignatureException.toAssert(void.class != type, "The method parameter type cannot be void");
                        parameterTypeList.add(type);
                    }
                } else {

                    refType.append(c);
                    if (c == ';') {
                        parameterTypeList.add(parseTypeSignatureClass(refType.toString()));
                        refType.delete(0, refType.length());
                    }
                }
            }
            IllegalMethodSignatureException.toAssert(refType.length() == 0, message);
            parameterTypes = parameterTypeList.toArray(new Class<?>[0]);
        }

        // 解析返回值类型
        String returnTypeSignature = methodSignature.substring(separatedIndex + 1);
        IllegalMethodSignatureException.toAssert(StringUtils.hasText(returnTypeSignature), message);
        Class<?> returnType = parseTypeSignatureClass(returnTypeSignature);
        return new LambdaMethodSignature(parameterTypes, returnType);
    }

    /**
     * @param typeSignature 如，引用类型 "Ljava/lang/String;"， 基本类型 "I" 或void "V"
     * @return
     */
    private static Class<?> parseTypeSignatureClass(String typeSignature) throws IllegalMethodSignatureException {

        if (StringUtils.hasText(typeSignature)) {

            // 基本类型或void
            if (typeSignature.length() == 1) {

                return parsePrimitiveTypeSignatureClass(typeSignature.charAt(0));
            } else if (typeSignature.startsWith("L") && typeSignature.endsWith(";")) {

                // 引用类型
                String returnTypeClassPath = typeSignature.substring(1, typeSignature.length() - 1);
                String returnTypeClassName = returnTypeClassPath.replace("/", ".");
                IllegalMethodSignatureException.toAssert(StringUtils.hasText(returnTypeClassName), String.format("Invalid type signature[%s]", typeSignature));
                try {
                    return ClassUtils.forName(returnTypeClassName, null);
                } catch (ClassNotFoundException e) {
                    throw new IllegalMethodSignatureException(String.format("Not found class of type[%s]", returnTypeClassPath));
                }
            }
        }
        throw new IllegalMethodSignatureException(String.format("Invalid type signature[%s] of lambda implemented method", typeSignature));
    }

    /**
     *
     * @param signature 基本类型 "I" 或void "V"
     * @return
     */
    private static Class<?> parsePrimitiveTypeSignatureClass(char signature) throws IllegalMethodSignatureException {

        Class<?> type = SIGNATURE_PRIMITIVE_TYPE_MAPPING.get(signature);
        if (type == null) {
            throw new IllegalMethodSignatureException(String.format("Invalid primitive type signature[%s] of lambda implemented method", signature));
        }
        return type;
    }

    private static String signType(Class<?> type) {

        Objects.requireNonNull(type);
        if (type.isPrimitive()) {
            return String.valueOf(PRIMITIVE_TYPE_SIGNATURE_MAPPING.get(type));
        } else {
            return "L" + type.getName().replace(".", "/") + ";";
        }
    }
}