package ws.spring.testdemo.beans;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ws.spring.beans.DefaultSingleBean;
import ws.spring.beans.SingleBean;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.pojo.Person;

import javax.validation.ConstraintViolationException;
import java.util.Collections;

/**
 * @author WindShadow
 * @version 2024-10-21.
 */
@Slf4j
public class SingleBeanTests extends SpringxAppTests {

    @Autowired
    private SingleBeanService sbs;

    @Test
    void validTest() {

        Exception e;

        e = Assertions.assertThrows(ConstraintViolationException.class, () -> sbs.consumeStringSingleBean(of("")));
        log.info("ConstraintViolationException: {}", e.getMessage());

        e = Assertions.assertThrows(ConstraintViolationException.class, () -> sbs.consumeIntegerSingleBean(of(10)));
        log.info("ConstraintViolationException: {}", e.getMessage());

        e = Assertions.assertThrows(ConstraintViolationException.class, () -> sbs.consumeCollectionSingleBean(of(Collections.emptySet())));
        log.info("ConstraintViolationException: {}", e.getMessage());

        e = Assertions.assertThrows(ConstraintViolationException.class, () -> sbs.consumePojoSingleBean(of(new Person("fake-email"))));
        log.info("ConstraintViolationException: {}", e.getMessage());

        Assertions.assertDoesNotThrow(() -> sbs.consumeStringSingleBean(of("abc")));
        Assertions.assertDoesNotThrow(() -> sbs.consumeIntegerSingleBean(of(100)));
        Assertions.assertDoesNotThrow(() -> sbs.consumeCollectionSingleBean(of(Collections.singleton(new Object()))));
        Assertions.assertDoesNotThrow(() -> sbs.consumePojoSingleBean(of(new Person("valid-email@ws.com"))));
    }

    private static  <T> SingleBean<T> of(T obj) {
        return new DefaultSingleBean<>(obj);
    }
}