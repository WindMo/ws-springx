package ws.spring.testdemo.mybatis.mapper;

import org.apache.ibatis.annotations.Select;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */

public interface EmployeeMapperByImport {

    @Select("select count(*) from employee")
    int countEmployees();
}
