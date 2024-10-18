package ws.spring.testdemo.jdbc.dao.main;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ws.spring.jdbc.dynamic.annotation.DataSource;
import ws.spring.testdemo.dao.DeptDaoOpt;
import ws.spring.testdemo.dao.EmployeeDaoOpt;
import ws.spring.testdemo.dao.IDeptDaoOpt;
import ws.spring.testdemo.dao.IEmployeeDaoOpt;
import ws.spring.testdemo.pojo.Dept;
import ws.spring.testdemo.pojo.Employee;

import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */

@Repository
public class DeptEmpAutoRepository implements IDeptDaoOpt, IEmployeeDaoOpt {

    private DeptDaoOpt deptDaoOpt;
    private EmployeeDaoOpt employeeDaoOpt;

    public DeptEmpAutoRepository(JdbcOperations jdbc) {

        this.deptDaoOpt = new DeptDaoOpt(jdbc);
        this.employeeDaoOpt = new EmployeeDaoOpt(jdbc);
    }

    @DataSource("dept")
    @Override
    public List<Dept> listAllDepts() {
        return deptDaoOpt.listAllDepts();
    }

    @DataSource("emp")
    @Override
    public List<Employee> listAllEmps() {
        return employeeDaoOpt.listAllEmps();
    }
}
