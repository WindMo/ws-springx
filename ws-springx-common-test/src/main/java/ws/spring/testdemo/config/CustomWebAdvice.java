package ws.spring.testdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.rest.response.RestResponse;

import javax.servlet.ServletException;

/**
 * @author WindShadow
 * @version 2024-10-22.
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class CustomWebAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServletException.class)
    public RestResponse<?> handlerServletException(ServletException e) {

        log.error("ServletException: {}", e.getMessage());
        return GlobalRest.FAILED.empty();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public RestResponse<?> handlerException(Exception e) {

        log.error("Exception: {}", e.getMessage());
        return GlobalRest.FAILED.empty();
    }
}
