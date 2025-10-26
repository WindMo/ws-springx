/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.lang.enums;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import ws.spring.lang.enums.EnumValue;
import ws.spring.lang.enums.EnumValues;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author WindShadow
 * @version 2022-02-19.
 * @see EnumValues
 */

@Slf4j
public class EnumValuesTests {

    @Test
    void getEnumTest() {

        Way way = EnumValues.getEnum(Way.class, Way.Up.getValue());
        Assertions.assertNotNull(way);
        Assertions.assertSame(Way.Up, way);

        List<Way> ways = EnumValues.getEnums(Way.class, Way.Up.getValue());
        Assertions.assertNotNull(ways);
        Assertions.assertSame(1, ways.size());
        Assertions.assertSame(Way.Up, ways.get(0));
    }

    @Test
    void getValueMapTest() {

        Map<Integer,Way> wayMap =  Stream.of(Way.Up).collect(Collectors.toMap(EnumValue::getValue, Function.identity()));
        Map<Integer,Way> map = EnumValues.getValueMap(Way.class);
        Assertions.assertNotNull(map);
        Assertions.assertEquals(wayMap, map);
    }

    @Test
    void getValueSetTest() {

        Set<Integer> values2 = Stream.of(Way.Up).map(EnumValue::getValue).collect(Collectors.toSet());
        Set<Integer> valueSet = EnumValues.getValueSet(Way.class);
        Assertions.assertEquals(values2, valueSet);
    }

    @Test
    void getValueListTest() {

        List<Integer> values = Stream.of(Way.Up).map(EnumValue::getValue).collect(Collectors.toList());
        List<Integer> valueList = EnumValues.getValueList(Way.class);
        Assertions.assertEquals(values, valueList);
    }

    @Test
    void complyWithTest() {

        Assertions.assertTrue(EnumValues.complyWith(Way.class,Way.Up.getValue()));
    }

    @Test
    void incorrectEnumTest() {

        IllegalArgumentException e;
        e = Assertions.assertThrows(IllegalArgumentException.class, () -> EnumValues.preCacheEnumValue(LogEnum.class));
        log.info("IllegalArgumentException: {}", e.getMessage());
        e = Assertions.assertThrows(IllegalArgumentException.class, () -> EnumValues.preCacheEnumValue(OperateSystem.class));
        log.info("IllegalArgumentException: {}", e.getMessage());
    }

    @Test
    void registerConverterTest() {

        ConfigurableConversionService service = new DefaultConversionService();
        EnumValues.registerEnumValueConverter(Way.class, service);

        Assertions.assertTrue(service.canConvert(Way.class, Integer.class));
        Assertions.assertTrue(service.canConvert(Integer.class, Way.class));
        Assertions.assertSame(Way.Up, service.convert(Way.Up.getValue(), Way.class));
        Assertions.assertSame(Way.Up.getValue(), service.convert(Way.Up, Integer.class));
    }
}
