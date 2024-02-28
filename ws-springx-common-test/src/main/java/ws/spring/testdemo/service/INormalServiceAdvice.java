package ws.spring.testdemo.service;

import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.annotation.PeekPoint;

/**
 * @author WindShadow
 * @version 2022-01-28.
 */

public interface INormalServiceAdvice {

    @PeekPoint
    ReturnValuePeeper<String> iout();
}
