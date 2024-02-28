package ws.spring.testdemo.text;

import lombok.extern.slf4j.Slf4j;
import ws.spring.text.annotaion.SqlEscape;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

@Slf4j
public class CustomTextServiceImpl implements CustomTextService {

    public String processSqlValue(@SqlEscape String value) {
        log.info("value: {}", value);
        return value;
    }

    public String processObjectValue(@SqlEscape Object value) {
        log.info("value: {}", value);
        return String.valueOf(value);
    }

    public String overrideProcessValue(@SqlEscape String value) {
        log.info("value: {}", value);
        return value;
    }

    @Override
    public String processMissEscape(String value) {
        log.info("value: {}", value);
        return value;
    }
}
