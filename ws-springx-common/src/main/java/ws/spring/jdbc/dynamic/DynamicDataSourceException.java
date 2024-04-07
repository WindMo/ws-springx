package ws.spring.jdbc.dynamic;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public class DynamicDataSourceException extends RuntimeException {

    public DynamicDataSourceException(String message) {
        super(message);
    }

    public DynamicDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
