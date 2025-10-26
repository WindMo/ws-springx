/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.testdemo.pojo.City;
import ws.spring.testdemo.pojo.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author WindShadow
 * @version 2023-06-01.
 * @see FormModelController
 */

public class FormModelControllerTests extends SpringxAppWebTests {

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

        MultiValueMap<String, String> emptyParams = new LinkedMultiValueMap<>();

        String result;
        result = request(get("/bind/extend/form-model/required").params(params));
        Assertions.assertEquals("" + user + city, result);

        result = request(get("/bind/extend/form-model/not-required").params(emptyParams));
        Assertions.assertEquals("" + null, result);

        result = request(get("/bind/extend/form-model/required/optional").params(params));
        Assertions.assertEquals("" + user, result);

        result = request(get("/bind/extend/form-model/not-required/optional").params(emptyParams));
        Assertions.assertEquals("" + null, result);

        // validation
        result = request(get("/bind/extend/form-model/required/validated").params(params));
        Assertions.assertEquals("" + user, result);

        MultiValueMap<String, String> invalidParams = new LinkedMultiValueMap<>(params);
        invalidParams.set("user.email", "fake-email");
        requestBytes(get("/bind/extend/form-model/required/validated").params(invalidParams), status().isBadRequest());
    }
}
