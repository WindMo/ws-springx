package ws.spring.testdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ws.spring.testdemo.web.rest.GlobalRest;

/**
 * @author WindShadow
 * @version 2024-10-22.
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class CustomWebAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (status.is4xxClientError()) {
            log.error("ClientError: {}", ex.getMessage());
        } else if (status.is5xxServerError()) {
            log.error("ServerError: {}", ex.getMessage());
        }
        return super.handleExceptionInternal(ex, GlobalRest.FAILED.of(ex.getMessage(), body), headers, status, request);
    }
}
