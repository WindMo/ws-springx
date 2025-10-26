/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.text;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ws.spring.text.annotaion.SqlEscape;

/**
 * @author WindShadow
 * @version 2023-06-08.
 */

@Slf4j
@Service
public class MainCustomTextServiceImpl extends CustomTextServiceImpl {

    @Override
    public String overrideProcessValue(String value) {
        return super.processSqlValue(value);
    }

    public String processMainValue(@SqlEscape String value) {
        log.info("value: {}", value);
        return String.valueOf(value);
    }
}
