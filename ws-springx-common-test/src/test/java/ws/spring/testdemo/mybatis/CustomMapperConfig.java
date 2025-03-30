package ws.spring.testdemo.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ws.spring.mybatis.mapper.EnhancedMapperFactory;
import ws.spring.mybatis.mapper.ImportMapper;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapper;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapperByImport;
import ws.spring.testdemo.mybatis.mapper.EmployeeMapperForImport;


@ImportMapper(mapper = EmployeeMapperByImport.class)
@ImportMapper(mapper = EmployeeMapperForImport.class, location = "classpath:/mapper/EmployeeMapperForImport.xml")
@TestConfiguration(proxyBeanMethods = false)
public class CustomMapperConfig {

    @Bean
    public FactoryBean<EmployeeMapper> employeeMapper(SqlSessionFactory sqlSessionFactory) {

        EnhancedMapperFactory<EmployeeMapper> mapperFactory = new EnhancedMapperFactory<>(EmployeeMapper.class);
        mapperFactory.setSqlSessionFactory(sqlSessionFactory);
        return mapperFactory;
    }
}
