package ws.spring.testdemo.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import ws.spring.testdemo.pojo.Employee;

import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@Slf4j
@RequiredArgsConstructor
public class EmployeeDaoOpt implements IEmployeeDaoOpt {

    private static final RowMapper<Employee> ROW_MAPPER = (rs, i) -> {

        Employee employee = new Employee();
        employee.setEmpId(rs.getInt("emp_id"));
        employee.setEmpName(rs.getString("emp_id"));
        return employee;
    };

    private final JdbcOperations jdbc;

    @Override
    public List<Employee> listAllEmps() {

        final String sql = "select `emp_id`, `emp_id` from employee";
        List<Employee> employees = jdbc.query(sql, ROW_MAPPER);
        log.info("employees: {}", employees);
        return employees;
    }
}
