package ws.spring.jdbc.dynamic.autoconfigure.sql;

import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */

@ConfigurationProperties("spring.ext.dynamic-datasource.init")
public class DynamicDataSourceSqlInitProperties {

    private boolean continueOnError = false;

    @NestedConfigurationProperty
    private SqlInitializationProperties defaultSqlLocation;
    private Map<String, SqlInitializationProperties> sqlLocations;

    public SqlInitializationProperties getDefaultSqlLocation() {
        return defaultSqlLocation;
    }

    public void setDefaultSqlLocation(SqlInitializationProperties defaultSqlLocation) {
        this.defaultSqlLocation = defaultSqlLocation;
    }

    public Map<String, SqlInitializationProperties> getSqlLocations() {
        return sqlLocations;
    }

    public boolean isContinueOnError() {
        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    public void setSqlLocations(Map<String, SqlInitializationProperties> sqlLocations) {
        this.sqlLocations = sqlLocations;
    }
}
