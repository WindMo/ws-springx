/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.pojo;

import lombok.*;

/**
 * @author WindShadow
 * @date 2021-11-21.
 */

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class City {

    private String name;
    private String desc;
}
