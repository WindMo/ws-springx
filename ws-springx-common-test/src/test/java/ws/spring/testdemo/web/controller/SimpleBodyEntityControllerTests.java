package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ws.spring.testdemo.SpringWebExtendApplicationTests;
import ws.spring.testdemo.util.JacksonUtils;
import ws.spring.web.rest.response.SimpleBodyEntity;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author WindShadow
 * @version 2023-07-08.
 * @see SimpleBodyEntityController
 */

public class SimpleBodyEntityControllerTests extends SpringWebExtendApplicationTests {

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
    }

    @Test
    void simpleResponseBodyTest() {

        Map<String, String> respJsonMap = request(get("/simple-body/response").param(key, value));
        Assertions.assertEquals(entityJsonMap, respJsonMap);
    }
}
