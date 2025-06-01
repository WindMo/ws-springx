package ws.spring.testdemo.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.util.JacksonUtils;
import ws.spring.web.entity.SingleEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author WindShadow
 * @version 2025-06-01.
 * @see SingleEntity
 */
@Slf4j
public class SimpleEntityTests extends SpringxAppTests {

    private final Random random = new Random();

    private String randomKey() {
        return "key" + random.nextInt(10);
    }

    private String randomValue() {
        return "value" + random.nextInt(10);
    }

    @Test
    void baseTest() {

        String entityJsonStr = JacksonUtils.toJson(Collections.singletonMap(randomKey(), randomValue()));
        Assertions.assertDoesNotThrow(() -> JacksonUtils.parse(entityJsonStr, new TypeReference<SingleEntity<Object>>() {
        }));

        Map<String, Integer> map = new HashMap<>();
        map.put("numA", 1);
        map.put("numB", 2);
        String json = JacksonUtils.toJson(map);

        Exception e;
        e = Assertions.assertThrows(RuntimeException.class, () -> JacksonUtils.parse(json, new TypeReference<SingleEntity<Object>>() {
        }));
        log.info("{}", e.getMessage());

        SingleEntity<String> entity = SingleEntity.of("KEY", "VALUE");
        String newKey = "key";
        Assertions.assertDoesNotThrow(() -> entity.replaceKey(newKey));
        Assertions.assertEquals(newKey, entity.getKey());
        e = Assertions.assertThrows(RuntimeException.class, () -> entity.replaceKey(null));
        log.info("{}", e.getMessage());
        e = Assertions.assertThrows(RuntimeException.class, () -> entity.replaceKey(""));
        log.info("{}", e.getMessage());

        String newValue = "value";
        entity.replaceValue(newValue);
        Assertions.assertEquals(newValue, entity.getValue());
    }
}
