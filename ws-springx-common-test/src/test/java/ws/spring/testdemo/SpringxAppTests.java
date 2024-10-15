package ws.spring.testdemo;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringxAppTests {

    public static void runInDuration(long duration, TimeUnit unit, Runnable runnable) {

        StopWatch sw = new StopWatch();
        sw.start();
        runnable.run();
        sw.stop();
        Assertions.assertTrue(sw.getTotalTimeMillis() <= unit.toMillis(duration));
    }
}
