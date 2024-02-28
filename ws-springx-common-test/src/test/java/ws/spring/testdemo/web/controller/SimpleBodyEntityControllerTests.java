package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.testdemo.util.JacksonUtils;
import ws.spring.web.entity.SimpleBodyEntity;

import java.util.Collections;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author WindShadow
 * @version 2023-07-08.
 * @see SimpleBodyEntityController
 */

public class SimpleBodyEntityControllerTests extends SpringxAppWebTests {

    private final String key = "name";
    private final String value = "tom";

    private Map<String, String> entityJsonMap;
    private String entityJsonStr;

    @BeforeEach
    public void initEach() {

        SimpleBodyEntity<String> entity = new SimpleBodyEntity<>(key, value);
        entityJsonMap = entity.fetchJsonMap();
        entityJsonStr = JacksonUtils.toJson(entityJsonMap);
    }

    @Test
    void simpleRequestBodyTest() {

        String respStr = request(post("/simple-body/request").content(entityJsonStr));
        Assertions.assertEquals(value, respStr);

        requestBytes(post("/simple-body/request").content(JacksonUtils.toJson(Collections.singletonMap("name", ""))),
                MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void simpleResponseBodyTest() {

        Map<String, String> respJsonMap = request(get("/simple-body/response").param(key, value));
        Assertions.assertEquals(entityJsonMap, respJsonMap);
    }
}
