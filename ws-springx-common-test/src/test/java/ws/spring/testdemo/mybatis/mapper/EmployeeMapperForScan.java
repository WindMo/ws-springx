/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */
@Mapper
public interface EmployeeMapperForScan {

    @Select("select count(*) from employee")
    int countEmployees();
}
