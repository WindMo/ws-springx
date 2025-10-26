/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.lang.enums;

import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 提供操作{@link EnumValue}的工具类
 *
 * @author WindShadow
 * @version 2022-02-19.
 * @see EnumValue
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class EnumValues {

    private static final Map<Class<? extends Enum>, Map<Object, Enum>> ENUM_VALUE_CACHE = new ConcurrentHashMap<>();

    private static Map<Object, Enum> cacheEnumValueIfNecessary(@NonNull Class<? extends Enum> enumType) {

        Assert.notNull(enumType, "The enumType must not be null");
        Assert.isAssignable(EnumValue.class, enumType);

        return ENUM_VALUE_CACHE.computeIfAbsent(enumType, type -> {
            EnumSet enumSet = EnumSet.allOf(enumType);
            Map<Object, Enum> valueMapTmp = new HashMap<>(enumSet.size() * 4 / 3 + 1);
            for (Object enumObj : enumSet) {

                Object enumValue = ((EnumValue) enumObj).getValue();
                Assert.notNull(enumValue,
                        () -> String.format("Enum type [%s] incorrectly implements interface <%s>. The enumValue of the enum <%s> is null",
                                enumType.getName(), EnumValue.class.getName(), enumObj));
                Assert.isTrue(!valueMapTmp.containsKey(enumValue),
                        () -> String.format("Enum type [%s] incorrectly implements interface <%s>. Enum <%s> and <%s> have the same enumValue: %s",
                                enumType.getName(), EnumValue.class.getName(), valueMapTmp.get(enumValue), enumObj, enumValue));
                valueMapTmp.put(enumValue, (Enum) enumObj);
            }
            return Collections.unmodifiableMap(valueMapTmp);
        });
    }

    private static <E extends Enum<? extends EnumValue<T>>, T> Map<Object, Enum> getEnumValueMap(@NonNull Class<E> enumType) {

        return cacheEnumValueIfNecessary(enumType);
    }

    private static <E extends Enum<? extends EnumValue<T>>, T> Set<Object> getValueObjects(@NonNull Class<E> enumType) {

        return getEnumValueMap(enumType).keySet();
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> void preCacheEnumValue(@NonNull Class<E> enumType) {

        cacheEnumValueIfNecessary(enumType);
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> E getEnum(@NonNull Class<E> enumType, @NonNull T value) {

        return (E) getEnumValueMap(enumType).get(Objects.requireNonNull(value));
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> List<E> getEnums(@NonNull Class<E> enumType, @NonNull T... values) {

        Assert.notEmpty(values, "The values must not be empty");
        Map<Object, Enum> enumMap = getEnumValueMap(enumType);
        List<E> list;
        if (values.length != 0) {
            list = new ArrayList<>();
            for (T t : values) {
                list.add((E) enumMap.get(t));
            }
        } else {
            list = Collections.emptyList();
        }
        return list;
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> Map<T, E> getValueMap(@NonNull Class<E> enumType) {

        return getEnumValueMap(enumType)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e -> (T) e.getKey(), e -> (E) e.getValue()));
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> Set<T> getValueSet(@NonNull Class<E> enumType) {

        return getValueObjects(enumType).stream().map(k -> (T) k).collect(Collectors.toSet());
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> List<T> getValueList(@NonNull Class<E> enumType) {

        return getValueObjects(enumType).stream().map(k -> (T) k).collect(Collectors.toList());
    }

    public static <E extends Enum<? extends EnumValue<T>>, T> boolean complyWith(@NonNull Class<E> enumType, @Nullable T value) {

        return value != null && getEnumValueMap(enumType).containsKey(value);
    }

    // ~ Converter support
    // =====================================================================================

    public static <E extends Enum<? extends EnumValue<T>>, T> void registerEnumValueConverter(@NonNull Class<E> enumType, ConverterRegistry registry) {

        Class<T> valueType = (Class<T>) ResolvableType.forClass(enumType)
                .as(EnumValue.class)
                .resolveGeneric(0);
        Assert.notNull(valueType, "Not found EnumValue interface on enumType: " + enumType);
        registry.addConverter(valueType, enumType, value -> getEnum(enumType, value));
        registry.addConverter(enumType, valueType, enumObj -> ((EnumValue<T>) enumObj).getValue());
    }
}
