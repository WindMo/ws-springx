package ws.spring.jdbc.dynamic;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源，特点是不支持应急机制，即根据数据源key无法索引到对应的数据源时，不使用默认数据源，而是以{@linkplain DataSourceNotFoundException 异常}告知
 *
 * @author WindShadow
 * @version 2024-03-31.
 */
public class DynamicDataSource extends AbstractDataSource {

    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final DataSourceSelector selector;

    @Nullable
    private DataSource defaultDataSource;

    public DynamicDataSource(DataSourceSelector selector) {
        this.selector = Objects.requireNonNull(selector);
    }

    public DataSourceSelector getSelector() {
        return selector;
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {

        Assert.notNull(defaultDataSource, "The defaultDataSource must noe be null");
        this.defaultDataSource = defaultDataSource;
    }

    public void putDataSource(String name, DataSource dataSource) {

        Assert.hasText(name, "The name of dataSource must noe be empty/null");
        Assert.notNull(dataSource, "The dataSource must noe be null");
        dataSources.merge(name, dataSource, (d1, d2) -> {
            throw new IllegalStateException(String.format("Duplicate name: %s of dataSource", name));
        });
    }

    protected final DataSource determineTargetDataSource() {

        String dataSourceName = selector.currentDataSource();
        DataSource dataSource;
        if (dataSourceName == null) {

            if (defaultDataSource == null) {
                throw new DynamicDataSourceException("The default data source of DynamicDataSource is not set");
            }
            dataSource = defaultDataSource;
        } else {
            dataSource = dataSources.get(dataSourceName);
            if (dataSource == null) {
                throw new DataSourceNotFoundException(dataSourceName);
            }
        }
        return dataSource;
    }

    // ~ implements
    // ==================================

    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return iface.isInstance(this) ? (T) this : determineTargetDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || determineTargetDataSource().isWrapperFor(iface);
    }
}
