package ws.spring.testdemo.jdbc.dao.fake;

import ws.spring.jdbc.dynamic.annotation.DataSource;

/**
 * @author WindShadow
 * @version 2024-04-07.
 */
@DataSource(IFakeDao.FAKE_DATA_SOURCE)
public interface IFakeDao {

    String FAKE_DATA_SOURCE = "FAKE_DATA_SOURCE";

    @DataSource(FAKE_DATA_SOURCE)
    void select(Runnable callBack);

    void update(Runnable callBack);

    void delete(Runnable firstCallBack, Runnable secondCallBack);
}
