package ws.spring.jdbc.dynamic;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public class DataSourceNotFoundException extends DynamicDataSourceException {

    public DataSourceNotFoundException(String dataSourceName) {
        super(String.format("Data source [%s] is not found", dataSourceName));
    }

    public DataSourceNotFoundException(String dataSourceName, Throwable cause) {
        super(String.format("Data source [%s] is not found", dataSourceName), cause);
    }
}
