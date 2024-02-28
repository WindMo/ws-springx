package ws.spring.testdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.aop.annotation.PeekPoint;
import ws.spring.aop.annotation.Peeper;

// 1、参数 (xxx) 返回值

/**
 * @author WindShadow
 * @version 2022-01-23.
 */

@Slf4j
@Peeper(value = NormalService.class)
@Component
public class NormalServiceAdvice implements INormalServiceAdvice {

    @PeekPoint()
    public ReturnValuePeeper<String> returnString() {

        log.info("method returnString - Args Advice");
        return (v, ex) -> log.info("return-Advice: {}, ex: {}", v, ex);
    }

    @PeekPoint(asyn = true)
    public ReturnValuePeeper<String> returnStringAsyn() {

        log.info("method returnStringAsyn - Args Advice");
        return (v, ex) -> log.info("return-Advice: {}, ex: {}", v, ex);
    }

    @Override
    public ReturnValuePeeper<String> iout() {

        log.info("method iout - Args Advice");
        return (v, ex) -> log.info("return-Advice: {}, ex: {}", v, ex);
    }

    @PeekPoint("consumerString")
    public ReturnValuePeeper<String> consumerString(String str) {

        log.info("method consumerString str: {} - Args Advice", str);
        return (v, ex) -> log.info("return-consumerString: {}, ex: {}", v, ex);
    }

    @PeekPoint("consumerString")
    public ReturnValuePeeper<String> consumerString() {

        log.info("method consumerString - Args Advice");
        return (v, ex) -> log.info("return-consumerString: {}, ex: {}", v, ex);
    }

    @PeekPoint("consumerString")
    public void consumerStringVoid(String str) {

        log.info("method consumerStringVoid str: {} - Args Advice", str);
    }

    @Order(1)
    @PeekPoint("consumerString")
    public void consumerStringVoid() {

        log.info("method consumerStringVoid - Advice");
    }
}
