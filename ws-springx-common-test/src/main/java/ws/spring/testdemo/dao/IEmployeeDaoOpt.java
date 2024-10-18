package ws.spring.testdemo.dao;

import ws.spring.jdbc.dynamic.annotation.DataSource;
import ws.spring.testdemo.pojo.Employee;

import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public interface IEmployeeDaoOpt {

    @DataSource("unknow")
    List<Employee> listAllEmps();
}
