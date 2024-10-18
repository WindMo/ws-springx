package ws.spring.testdemo.jdbc.dao.main;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ws.spring.jdbc.dynamic.annotation.DataSource;
import ws.spring.testdemo.dao.DeptDaoOpt;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@DataSource("dept")
@Repository
public class DeptDaoAutoRepository extends DeptDaoOpt {
    public DeptDaoAutoRepository(JdbcOperations jdbc) {
        super(jdbc);
    }
}
