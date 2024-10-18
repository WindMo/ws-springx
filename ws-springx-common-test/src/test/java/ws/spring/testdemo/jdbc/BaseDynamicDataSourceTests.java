package ws.spring.testdemo.jdbc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;
import ws.spring.testdemo.SpringxAppTests;

import java.util.Random;


class BaseDynamicDataSourceTests extends SpringxAppTests {

    private static final Random RANDOM = new Random();

    @Autowired
    protected SwitchableDataSourceSelector selector;

    @AfterEach
    void reset() {
        selector.resetSelected();
    }

    protected void expectDataSourceName(String name) {
        Assertions.assertEquals(name, selector.currentDataSource());
    }

    protected static String genFakeDataSourceName() {
        return "fake-name-" + RANDOM.nextInt(100);
    }
}
