/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.pojo;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2025-07-13.
 */
@NoArgsConstructor
@Data
@ToString
@Table("employee")
public class Employee {

    private Integer empId;
    private String empName;
}
