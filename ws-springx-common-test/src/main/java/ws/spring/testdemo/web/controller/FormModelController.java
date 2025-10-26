/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.spring.testdemo.pojo.City;
import ws.spring.testdemo.pojo.User;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.bind.annotation.FormModel;
import ws.spring.web.rest.response.RestResponse;

import java.util.Optional;

/**
 * @author WindShadow
 * @version 2023-06-01.
 */

@RestController
@RequestMapping("/bind/extend/form-model")
public class FormModelController {

    @GetMapping("/required")
    public RestResponse<String> formModelBindRequired(@FormModel User user, @FormModel City city) {

        return GlobalRest.SUCCESS.of("" + user + city);
    }

    @GetMapping("/not-required")
    public RestResponse<String> formModelBindNoRequired(@FormModel(required = false) User user) {

        return GlobalRest.SUCCESS.of("" + user);
    }

    @GetMapping("/required/optional")
    public RestResponse<String> formModelBindRequiredOptional(@FormModel Optional<User> user) {

        return GlobalRest.SUCCESS.of("" + user.get());
    }

    @GetMapping("/not-required/optional")
    public RestResponse<String> formModelBindNoRequiredOptional(@FormModel(required = false) Optional<User> user) {

        return GlobalRest.SUCCESS.of("" + user.orElse(null));
    }

    @GetMapping("/required/validated")
    public RestResponse<String> formModelBindRequiredValidated(@Validated @FormModel User user) {

        return GlobalRest.SUCCESS.of("" + user);
    }
}
