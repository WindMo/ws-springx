package ws.spring.testdemo.mybatis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapper;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapperByImport;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapperForImport;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapperForScan;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */
@ActiveProfiles("datasource")
@Import(CustomMapperConfig.class)
public class EnhancedMapperFactoryTests extends SpringxAppTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void enhancedMapperFactoryRegisterTest() {

        Assertions.assertDoesNotThrow(() -> context.getBean(EmployeeMapperForScan.class).countEmployees());
        Assertions.assertDoesNotThrow(() -> context.getBean(EmployeeMapper.class).countEmployees());
        Assertions.assertDoesNotThrow(() -> context.getBean(EmployeeMapperForImport.class).countEmployees());
        Assertions.assertDoesNotThrow(() -> context.getBean(EmployeeMapperByImport.class).countEmployees());
    }
}
