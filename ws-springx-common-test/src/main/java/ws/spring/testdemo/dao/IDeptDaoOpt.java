package ws.spring.testdemo.dao;

import ws.spring.jdbc.dynamic.annotation.DataSource;
import ws.spring.testdemo.pojo.Dept;

import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public interface IDeptDaoOpt {

    @DataSource("unknow")
    List<Dept> listAllDepts();
}
