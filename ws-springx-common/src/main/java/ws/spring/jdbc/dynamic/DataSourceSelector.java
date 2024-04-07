package ws.spring.jdbc.dynamic;

import org.springframework.lang.Nullable;

/**
 * 数据源选择器，但通常使用{@link SwitchableDataSourceSelector}
 * @author WindShadow
 * @version 2024-03-31.
 */
public interface DataSourceSelector {

    /**
     * @return 当前使用的数据源名称
     */
    @Nullable
    String currentDataSource();
}
