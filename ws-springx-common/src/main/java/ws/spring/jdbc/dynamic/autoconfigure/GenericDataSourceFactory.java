package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

public class GenericDataSourceFactory implements DataSourceFactory<DataSourceProperties> {

    @Override
    public DataSource buildDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}
