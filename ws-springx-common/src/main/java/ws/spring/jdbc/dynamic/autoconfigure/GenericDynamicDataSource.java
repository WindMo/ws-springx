package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import ws.spring.jdbc.dynamic.DataSourceFactory;
import ws.spring.jdbc.dynamic.DataSourceSelector;
import ws.spring.jdbc.dynamic.DynamicDataSource;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */
public class GenericDynamicDataSource extends DynamicDataSource<DataSourceProperties>  {

    public GenericDynamicDataSource(DataSourceSelector selector, DataSourceFactory<DataSourceProperties> factory) {
        super(selector, DataSourceProperties.class, factory);
    }
}
