package ws.spring.testdemo.jdbc.dao.fake;

/**
 * @author WindShadow
 * @version 2024-04-07.
 */
class FakeDaoImpl implements IFakeDao {

    @Override
    public void select(Runnable callBack) {
        callBack.run();
    }

    @Override
    public void update(Runnable callBack) {
        callBack.run();
    }

    @Override
    public void delete(Runnable firstCallBack, Runnable secondCallBack) {
        firstCallBack.run();
        secondCallBack.run();
    }
}
