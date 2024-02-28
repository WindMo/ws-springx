package ws.spring.testdemo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.entity.SimpleBodyEntity;
import ws.spring.web.rest.response.RestResponse;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author WindShadow
 * @version 2023-07-08.
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/simple-body")
public class SimpleBodyEntityController {

    @PostMapping("/request")
    public RestResponse<String> simpleRequestBody(@Valid @RequestBody SimpleBodyEntity<@NotBlank String> reqBody) {

        log.info("reqBody: {}", reqBody);
        return GlobalRest.SUCCESS.of(reqBody.getValue());
    }

    @GetMapping("/response")
    public RestResponse<SimpleBodyEntity<String>> simpleResponseBody(@RequestParam("name") String name) {

        log.info("name: {}", name);
        return GlobalRest.SUCCESS.of(new SimpleBodyEntity<>("name", name));
    }
}
