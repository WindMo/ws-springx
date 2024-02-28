package ws.spring.testdemo.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.spring.testdemo.pojo.City;
import ws.spring.testdemo.pojo.User;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.bind.FormModel;
import ws.spring.web.rest.response.RestResponse;

/**
 * @author WindShadow
 * @version 2023-06-01.
 */

@RestController
@RequestMapping("/bind/extend/form-model")
public class FormModelController {

    @GetMapping("")
    public RestResponse<String> formModelBind(@FormModel User user, @FormModel City city) {

        return GlobalRest.SUCCESS.of("" + user + city);
    }
}
