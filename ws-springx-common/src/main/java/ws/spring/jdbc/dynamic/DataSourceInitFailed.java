package ws.spring.jdbc.dynamic;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */
public class DataSourceInitFailed extends DynamicDataSourceException {

    public DataSourceInitFailed(String message) {
        super(message);
    }

    public DataSourceInitFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
