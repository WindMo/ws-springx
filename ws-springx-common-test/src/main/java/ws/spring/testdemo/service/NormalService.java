package ws.spring.testdemo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ws.spring.aop.annotation.ExposurePoint;


/**
 * @author WindShadow
 * @version 2022-01-22.
 */

@Slf4j
@Service
public class NormalService implements INormalService {

    @ExposurePoint
    public String returnString() {

        log.info("method returnString");
        return "returnString";
    }

    @ExposurePoint
    public String returnStringAsyn() {

        log.info("method returnStringAsyn");
        return "returnStringAsyn";
    }

    @Override
    public String iout() {
        log.info("method iout");
        return "iout";
    }

    @ExposurePoint("consumerString")
    public String consumerString(String str) {

        log.info("consumerString, str : {}", str);
        return "consumerString: " + str;
    }
}
