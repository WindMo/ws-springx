package ws.spring.testdemo.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import ws.spring.jdbc.dynamic.DataSourceNotFoundException;
import ws.spring.testdemo.dao.IDeptDaoOpt;
import ws.spring.testdemo.dao.IEmployeeDaoOpt;
import ws.spring.testdemo.jdbc.dao.main.*;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */
@Slf4j
@ActiveProfiles("dynamic")
public class MainDynamicDataSourceTests extends BaseDynamicDataSourceTests {

    private final String dataSourceDept = "dept";
    private final String dataSourceEmployee = "emp";

    @Autowired
    private ApplicationContext context;

    private IDeptDaoOpt deptDaoOpt;

    private IEmployeeDaoOpt empDao;

    @BeforeEach
    void resetSelector() {
        selector.resetSelected();
    }

    @Test
    void notFoundDataSourceTest() {

        useRepository();
        Exception e;
        e = Assertions.assertThrows(DataSourceNotFoundException.class,
                () -> selector.runWith(genFakeDataSourceName(), deptDaoOpt::listAllDepts));
        log.info(e.getMessage());
    }

    @Test
    void useDefaultDataSourceTest() {

        useRepository();
        Assertions.assertDoesNotThrow(() -> selector.runWithDefault(deptDaoOpt::listAllDepts));
        Assertions.assertDoesNotThrow(deptDaoOpt::listAllDepts);
    }

    @Test
    void manualSwitchTest() {

        useRepository();
        Exception e;
        e = Assertions.assertThrows(DataAccessException.class, () -> selector.runWith(dataSourceEmployee, deptDaoOpt::listAllDepts));
        log.info(e.getMessage());
        Assertions.assertDoesNotThrow(() -> selector.runWith(dataSourceDept, deptDaoOpt::listAllDepts));
        Assertions.assertDoesNotThrow(() -> selector.runWith(dataSourceEmployee, empDao::listAllEmps));
    }

    @Test
    void classAnnotationSwitchTest() {

        useAutoRepository();
        annotationSwitchTest();
    }

    @Test
    void methodAnnotationSwitchTest() {

        userCompositeRepository();
        annotationSwitchTest();
    }

    private void annotationSwitchTest() {

        Assertions.assertDoesNotThrow(deptDaoOpt::listAllDepts);
        Assertions.assertDoesNotThrow(empDao::listAllEmps);
    }

    // ~ inner methods
    // ==================================


    private void useRepository() {

        deptDaoOpt = context.getBean(DeptDaoRepository.class);
        empDao = context.getBean(EmployeeDaoRepository.class);
    }

    private void useAutoRepository() {

        deptDaoOpt = context.getBean(DeptDaoAutoRepository.class);
        empDao = context.getBean(EmployeeDaoAutoRepository.class);
    }

    private void userCompositeRepository() {

        DeptEmpAutoRepository repository = context.getBean(DeptEmpAutoRepository.class);
        deptDaoOpt = repository;
        empDao = repository;
    }

    @ComponentScan("ws.spring.testdemo.jdbc.dao.main")
    protected static class BaseTestConfig {}
}
