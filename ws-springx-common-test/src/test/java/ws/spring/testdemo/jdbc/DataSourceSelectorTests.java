package ws.spring.testdemo.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import ws.spring.testdemo.jdbc.dao.fake.DefaultFakeDao;
import ws.spring.testdemo.jdbc.dao.fake.FakeDao4Method;
import ws.spring.testdemo.jdbc.dao.fake.IFakeDao;

/**
 * @author WindShadow
 * @version 2024-04-07.
 */
@Slf4j
@SpringBootTest(properties = {
        "spring.ext.dynamic-datasource.enabled=false",
        "spring.data.jdbc.repositories.enabled=false"
        })
@Import(DataSourceSelectorTests.BaseTestConfiguration.class)
public class DataSourceSelectorTests extends BaseDynamicDataSourceTests {

    @Autowired
    private ApplicationContext context;

    private IFakeDao fakeDao;

    private <T extends IFakeDao> void useFakeDao(Class<T> clazz) {
        fakeDao = context.getBean(clazz);
    }

    /**
     * 手动选择数据源
     */
    @Test
    void manualSelectorTest() {

        selector.resetSelected();
        expectDataSourceName(null);

        selector.useDefaultDataSource();
        expectDataSourceName(null);

        String name = genFakeDataSourceName();
        selector.selectedDataSource(name);
        expectDataSourceName(name);

        selector.runWith(name, () -> expectDataSourceName(name));
        selector.runWithDefault(() -> expectDataSourceName(null));

        String currentName = name + "-current";
        selector.selectedDataSource(currentName);
        selector.runWith(name, () -> expectDataSourceName(name));
        expectDataSourceName(currentName);

        Exception e;
        e = Assertions.assertThrows(IllegalArgumentException.class, () -> selector.runWith(null, () -> {}));
        log.info(e.getMessage());
    }

    /**
     * 子类不继承使用父类或接口指定的<code>DataSource</code>
     */
    @Test
    void defaultSelectTest() {

        useFakeDao(DefaultFakeDao.class);
        fakeDao.select(() -> expectDataSourceName(null));
        fakeDao.update(() -> expectDataSourceName(null));
    }

    /**
     * 优先使用方法上的<code>DataSource</code>，方法上无<code>DataSource</code>时，使用类上的
     */
    @Test
    void methodSelectTest() {

        useFakeDao(FakeDao4Method.class);
        fakeDao.select(() -> expectDataSourceName(FakeDao4Method.METHOD_DATA_SOURCE));
        fakeDao.update(() -> expectDataSourceName(FakeDao4Method.DATA_SOURCE));
        fakeDao.delete(() -> fakeDao.update(() -> expectDataSourceName(FakeDao4Method.DATA_SOURCE)), () -> expectDataSourceName(FakeDao4Method.METHOD_DATA_SOURCE));
    }


    @ComponentScan("ws.spring.testdemo.jdbc.dao.fake")
    @Configuration
    protected static class BaseTestConfiguration implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{"ws.spring.jdbc.dynamic.autoconfigure.DataSourceSwitchConfiguration"};
        }
    }
}
