/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.text;

import ws.spring.text.annotaion.SqlEscape;

/**
 * @author WindShadow
 * @version 2023-06-07.
 */

public interface CustomTextService {

    String processMissEscape(@SqlEscape String value);
}
