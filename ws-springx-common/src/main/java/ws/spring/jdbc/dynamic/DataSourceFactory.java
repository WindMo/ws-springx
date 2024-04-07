package ws.spring.jdbc.dynamic;

import javax.sql.DataSource;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@FunctionalInterface
public interface DataSourceFactory<T> {

    DataSource buildDataSource(T properties);
}
