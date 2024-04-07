package ws.spring.jdbc.dynamic.support;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */
public class ThreadLocalDataSourceSelector extends BaseDataSourceSelector {

    private final ThreadLocal<DataSourceName> local;

    public ThreadLocalDataSourceSelector(boolean inheritable) {
        this.local = inheritable ? new InheritableThreadLocal<>() : new ThreadLocal<>();
    }

    @Override
    protected DataSourceName getCurrentName() {
        return local.get();
    }

    @Override
    protected void saveCurrentName(DataSourceName name) {
        local.set(name);
    }

    @Override
    protected void removeCurrentName() {
        local.remove();
    }
}
