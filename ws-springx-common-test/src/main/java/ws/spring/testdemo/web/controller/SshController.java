package ws.spring.testdemo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.rest.response.RestResponse;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */

@RestController
public class SshController {

    @GetMapping("/ssh")
    public RestResponse<String> hello() {

        return GlobalRest.SUCCESS.of("hello-ssh");
    }
}
