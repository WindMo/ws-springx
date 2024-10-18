package ws.spring.testdemo.jdbc.dao.fake;

import org.springframework.boot.test.context.TestComponent;
import ws.spring.jdbc.dynamic.annotation.DataSource;

/**
 * @author WindShadow
 * @version 2024-04-07.
 */

@TestComponent
@DataSource(FakeDao4Method.DATA_SOURCE)
public class FakeDao4Method extends FakeDaoImpl {

    public static final String DATA_SOURCE = "FakeDao.DATA_SOURCE";
    public static final String METHOD_DATA_SOURCE = "FakeDao.METHOD_DATA_SOURCE";

    @DataSource(METHOD_DATA_SOURCE)
    @Override
    public void select(Runnable callBack) {
        super.select(callBack);
    }

    @Override
    public void update(Runnable callBack) {
        super.select(callBack);
    }

    @DataSource(METHOD_DATA_SOURCE)
    @Override
    public void delete(Runnable firstCallBack, Runnable secondCallBack) {
        super.delete(firstCallBack, secondCallBack);
    }
}
