package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ws.spring.testdemo.SpringWebExtendApplicationTests;
import ws.spring.testdemo.pojo.City;
import ws.spring.testdemo.pojo.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author WindShadow
 * @version 2023-06-01.
 * @see FormModelController
 */

public class FormModelControllerTests extends SpringWebExtendApplicationTests {

    @Test
    void formModelBindTest() {

        User user = new User("tom", 18, "tom-cat", "123@qq.com");
        City city = new City("北京", "中国首都");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("user.name", user.getName());
        params.add("user.age", "" + user.getAge());
        params.add("user.desc", user.getDesc());
        params.add("user.email", user.getEmail());
        params.add("city.name", city.getName());
        params.add("city.desc", city.getDesc());

        String result = request(get("/bind/extend/form-model").params(params));
        Assertions.assertEquals("" + user + city, result);
    }
}
