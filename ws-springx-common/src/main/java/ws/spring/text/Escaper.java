/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.text;

/**
 * @author WindShadow
 * @version 2023-06-06.
 */

@FunctionalInterface
public interface Escaper {

    String escape(String value);
}
