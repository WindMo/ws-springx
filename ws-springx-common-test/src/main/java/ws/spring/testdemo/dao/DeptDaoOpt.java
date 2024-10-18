package ws.spring.testdemo.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import ws.spring.testdemo.pojo.Dept;

import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@Slf4j
@RequiredArgsConstructor
public class DeptDaoOpt implements IDeptDaoOpt {

    private static final RowMapper<Dept> ROW_MAPPER = (rs, i) -> {

        Dept dept = new Dept();
        dept.setId(rs.getInt("id"));
        dept.setName(rs.getString("name"));
        return dept;
    };

    private final JdbcOperations jdbc;

    @Override
    public List<Dept> listAllDepts() {

        final String sql = "select `id`, `name` from dept";
        List<Dept> depts = jdbc.query(sql, ROW_MAPPER);
        log.info("depts: {}", depts);
        return depts;
    }
}
