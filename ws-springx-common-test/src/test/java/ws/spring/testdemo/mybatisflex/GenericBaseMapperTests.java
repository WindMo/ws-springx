/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.mybatisflex;

import com.mybatisflex.core.query.QueryWrapper;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ws.spring.mybatis.mapper.ImportMapper;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.mybatisflex.mapper.EmployeeBaseMapper;
import ws.spring.testdemo.pojo.Employee;

/**
 * @author WindShadow
 * @version 2025-07-13.
 */
@ActiveProfiles({"datasource", "mybatisflex"})
@ImportMapper(mapper = EmployeeBaseMapper.class)
public class GenericBaseMapperTests extends SpringxAppTests {

    @Autowired
    private EmployeeBaseMapper employeeMapper;

    @Test
    void selectOneTest() {

        Employee tom = employeeMapper.selectOneByQuery(QueryWrapper.create().where(Employee::getEmpId).eq(1001));
        Assertions.assertEquals("tom", tom.getEmpName());
        Assertions.assertThrows(TooManyResultsException.class, () -> employeeMapper.selectOneByQuery(QueryWrapper.create()));
        Assertions.assertThrows(TooManyResultsException.class, () -> employeeMapper.selectOneByQueryAs(QueryWrapper.create(), Employee.class));
        Assertions.assertThrows(TooManyResultsException.class, () -> employeeMapper.selectOneWithRelationsByQuery(QueryWrapper.create()));
        Assertions.assertThrows(TooManyResultsException.class, () -> employeeMapper.selectOneWithRelationsByQueryAs(QueryWrapper.create(), Employee.class));
    }
}
