/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.text;

import org.springframework.util.StringUtils;

/**
 * @author WindShadow
 * @version 2023-06-06.
 */

public class SqlEscaper implements Escaper {

    public static String escapeSqlValue(String value) {

        if (StringUtils.hasText(value)) {

            StringBuilder sb = new StringBuilder();
            for (char c : value.toCharArray()) {

                if (c == '_' || c == '%' || c == '\\') {
                    sb.append('\\');
                }
                sb.append(c);
            }
            return sb.toString();
        }
        return value;
    }

    @Override
    public String escape(String value) {
        return escapeSqlValue(value);
    }
}
