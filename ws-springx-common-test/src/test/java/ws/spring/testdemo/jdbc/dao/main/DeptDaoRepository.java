package ws.spring.testdemo.jdbc.dao.main;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ws.spring.testdemo.dao.DeptDaoOpt;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@Repository
public class DeptDaoRepository extends DeptDaoOpt {
    public DeptDaoRepository(JdbcOperations jdbc) {
        super(jdbc);
    }
}
