package ws.spring.testdemo.jdbc.dao.main;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ws.spring.jdbc.dynamic.annotation.DataSource;
import ws.spring.testdemo.dao.EmployeeDaoOpt;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */
@DataSource("emp")
@Repository
public class EmployeeDaoAutoRepository extends EmployeeDaoOpt {
    public EmployeeDaoAutoRepository(JdbcOperations jdbc) {
        super(jdbc);
    }
}
