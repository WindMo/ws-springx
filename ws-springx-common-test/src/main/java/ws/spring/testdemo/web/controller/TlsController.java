/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.rest.response.RestResponse;

/**
 * @author WindShadow
 * @version 2022-10-02.
 */

@RestController
public class TlsController {

    @GetMapping("/ssl")
    public RestResponse<String> hello() {

        return GlobalRest.SUCCESS.of("hello-ssl");
    }
}
