package ws.spring.jdbc.dynamic;

/**
 * 当动态数据源的默认数据源未配置，但在程序中被选中时，由此异常通知
 *
 * @author WindShadow
 * @version 2024-04-06.
 */
public class DefaultDataSourceNotConfiguredException extends DynamicDataSourceException {

    DefaultDataSourceNotConfiguredException() {
        super("The default source is not configured");
    }
}
