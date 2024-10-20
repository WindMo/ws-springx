package ws.spring.jdbc.dynamic.autoconfigure.sql;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.sql.init.DatabaseInitializationMode;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import ws.spring.jdbc.dynamic.DataSourceSelector;
import ws.spring.jdbc.dynamic.DynamicDataSource;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */

public class DynamicDataSourceScriptInitializer extends SqlDataSourceScriptDatabaseInitializer {

    protected final Log logger = LogFactory.getLog(this.getClass());

    @Nullable
    private final SwitchableDataSourceSelector dataSourceSelector;
    private volatile ResourceLoader resourceLoader;
    private DynamicDataSourceSqlInitProperties properties;

    public DynamicDataSourceScriptInitializer(DynamicDataSource dataSource, DynamicDataSourceSqlInitProperties properties) {
        super(dataSource, disableInitProperties());
        this.properties = properties;
        DataSourceSelector selector = dataSource.getSelector();
        this.dataSourceSelector = selector instanceof SwitchableDataSourceSelector ? (SwitchableDataSourceSelector) selector : null;
    }

    public DynamicDataSourceSqlInitProperties getProperties() {
        return properties;
    }

    public void setProperties(DynamicDataSourceSqlInitProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        super.setResourceLoader(resourceLoader);
        this.resourceLoader = resourceLoader;
    }

    @Override
    public boolean initializeDatabase() {

        SqlInitializationProperties defaultSqlLocation = properties.getDefaultSqlLocation();
        if (defaultSqlLocation != null) {
            if (!initializeDatabase(null, defaultSqlLocation) && !properties.isContinueOnError()) {
                return false;
            }
        }

        Map<String, SqlInitializationProperties> sqlLocations = properties.getSqlLocations();
        if (dataSourceSelector != null && !CollectionUtils.isEmpty(sqlLocations)) {
            for (Map.Entry<String, SqlInitializationProperties> propertiesEntry : sqlLocations.entrySet()) {

                String name = propertiesEntry.getKey();
                SqlInitializationProperties sqlLocation = propertiesEntry.getValue();
                if (!initializeDatabase(name, sqlLocation) && !properties.isContinueOnError()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean initializeDatabase(@Nullable String name, SqlInitializationProperties sqlLocation) {

        try {

            Callable<Boolean> callable = () -> {

                SqlDataSourceScriptDatabaseInitializer initializer = new SqlDataSourceScriptDatabaseInitializer(getDataSource(), sqlLocation);
                initializer.setResourceLoader(resourceLoader);
                return initializer.initializeDatabase();
            };
            return name == null ? dataSourceSelector.runWithDefault(callable) : dataSourceSelector.runWith(name, callable);
        } catch (Exception e) {
            this.logger.error(String.format("Error while initialize database [%s]", name), e);
            return false;
        }
    }

    private static DatabaseInitializationSettings disableInitProperties() {

        DatabaseInitializationSettings properties = new DatabaseInitializationSettings();
        properties.setMode(DatabaseInitializationMode.NEVER);
        return properties;
    }
}
