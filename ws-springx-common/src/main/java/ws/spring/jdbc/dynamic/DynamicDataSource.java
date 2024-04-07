package ws.spring.jdbc.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * 动态数据源，其具有以下特点：
 * <ul>
 *     <li>数据源的标识key必须是字符串</li>
 *     <li>数据源类原始对象仅限以下类型：String类型（从可能的{@link DataSourceLookup}解析出数据源）、{@linkplain DataSource 真实数据源}、{@linkplain #dataSourcePropertiesType 数据源配置类型}</li>
 *     <li>必须配置默认数据源</li>
 *     <li>不支持应急机制，即根据数据源key无法索引到对应的数据源时，不使用默认数据源，而是以{@linkplain DataSourceNotFoundException 异常}告知</li>
 * </ul>
 *
 * @author WindShadow
 * @version 2024-03-31.
 * @param <T> 数据源配置类型，需要对应的{@linkplain DataSourceFactory 数据源工厂}创建为可用的数据源
 */
public class DynamicDataSource<T> extends AbstractRoutingDataSource {

    private final DataSourceSelector selector;
    private final Class<T> dataSourcePropertiesType;
    private final DataSourceFactory<T> factory;

    public DynamicDataSource(DataSourceSelector selector, Class<T> dataSourcePropertiesType, DataSourceFactory<T> factory) {
        this.selector = Objects.requireNonNull(selector);
        this.dataSourcePropertiesType = Objects.requireNonNull(dataSourcePropertiesType);
        this.factory = Objects.requireNonNull(factory);
        super.setLenientFallback(false);
    }

    @Override
    public final void setLenientFallback(boolean lenientFallback) {
        throw new UnsupportedOperationException("The default data source must be configured in the DynamicDataSource, so it is not allowed fallback");
    }

    @Override
    public final void setDefaultTargetDataSource(Object defaultTargetDataSource) {

        Assert.notNull(defaultTargetDataSource, "The defaultTargetDataSource must noe be null");
        checkDataSourceType(defaultTargetDataSource);
        super.setDefaultTargetDataSource(defaultTargetDataSource);
    }

    @Override
    public final void setTargetDataSources(Map<Object, Object> targetDataSources) {

        Assert.notNull(targetDataSources, "The targetDataSources must noe be null");
        targetDataSources.forEach((key, dataSource) -> {

            Assert.notNull(key, "The key of targetDataSources must not be null");
            Assert.isInstanceOf(String.class, key, () -> String.format("The key[%s] of targetDataSources must be String", key));
            Assert.hasText((String) key, "The key of targetDataSources must not be empty");
            checkDataSourceType(dataSource);
        });
        super.setTargetDataSources(targetDataSources);
    }

    @Override
    public final void afterPropertiesSet() {

        super.afterPropertiesSet();
        if (getResolvedDefaultDataSource() == null) {
            throw new DefaultDataSourceNotConfiguredException();
        }
    }

    /**
     * @return 永远为代表数据源名称的String类型
     */
    @Override
    protected final Object determineCurrentLookupKey() {
        return selector.currentDataSource();
    }

    @Override
    protected final Object resolveSpecifiedLookupKey(Object lookupKey) {

        Assert.state(lookupKey instanceof String, "The lookupKey type must be String");
        Assert.state(StringUtils.hasText((String) lookupKey), "The lookupKey must not empty");
        return lookupKey;
    }

    @Override
    protected final DataSource resolveSpecifiedDataSource(Object dataSource) throws IllegalArgumentException {

        checkDataSourceType(dataSource);
        if (dataSourcePropertiesType.isInstance(dataSource)) {

            T properties = (T) dataSource;
            return factory.buildDataSource(properties);
        }
        return super.resolveSpecifiedDataSource(dataSource);
    }

    @Override
    protected final DataSource determineTargetDataSource() {

        try {
            return super.determineTargetDataSource();
        } catch (IllegalStateException e) {
            throw new DataSourceNotFoundException((String) determineCurrentLookupKey(), e);
        }
    }

    protected final void checkDataSourceType(Object dataSource) {

        Assert.isTrue(acceptDataSourceType(dataSource),
                () -> String.format("Special data source type[%s] that are not supported. Only the following types<%s, %s, %s> are supported",
                        dataSource.getClass().getName(), String.class.getName(), DataSource.class.getName(), dataSourcePropertiesType.getName()));
    }

    public final boolean acceptDataSourceType(Object dataSource) {

        return dataSource instanceof String
                || dataSource instanceof DataSource
                || dataSourcePropertiesType.isInstance(dataSource);
    }
}
