package ws.spring.testdemo.jdbc.dao.main;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ws.spring.testdemo.dao.EmployeeDaoOpt;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@Repository
public class EmployeeDaoRepository extends EmployeeDaoOpt {
    public EmployeeDaoRepository(JdbcOperations jdbc) {
        super(jdbc);
    }
}
