package ws.spring.testdemo.service;

import ws.spring.aop.annotation.ExposurePoint;

/**
 * @author WindShadow
 * @version 2022-01-25.
 */

public interface INormalService {

    @ExposurePoint
    String iout();
}
