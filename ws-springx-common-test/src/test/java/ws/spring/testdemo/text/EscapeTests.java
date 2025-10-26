/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.text;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.text.SqlEscaper;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */
@Slf4j
public class EscapeTests extends SpringxAppTests {

    @Autowired
    private MainCustomTextServiceImpl customTextService;

    @Test
    void escapeSqlTest() {

        String value = "abc%123_xyz\\mn\\\\";
        Assertions.assertEquals(SqlEscaper.escapeSqlValue(value), customTextService.processMainValue(value));
        Assertions.assertEquals(SqlEscaper.escapeSqlValue(value), customTextService.processSqlValue(value));
        Assertions.assertEquals(SqlEscaper.escapeSqlValue(value), customTextService.processObjectValue(value));
        Assertions.assertEquals("null", customTextService.processObjectValue(null));
        Exception exception = Assertions.assertThrows(IllegalStateException.class, () -> customTextService.processObjectValue(new Object()));
        log.error(exception.getMessage());
        // 方法重写时失效
        Assertions.assertEquals(value, customTextService.overrideProcessValue(value));
        // 继承接口时失效
        Assertions.assertEquals(value, customTextService.processMissEscape(value));
    }
}
