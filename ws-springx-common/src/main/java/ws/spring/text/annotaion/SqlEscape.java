/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.text.annotaion;

import ws.spring.text.SqlEscaper;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2023-06-06.
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Escape(SqlEscaper.class)
public @interface SqlEscape {
}
