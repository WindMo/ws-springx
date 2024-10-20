package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;


@ConfigurationProperties("spring.ext.dynamic-datasource")
public class DynamicDataSourceProperties {

    private boolean enabled = false;

    @NestedConfigurationProperty
    private DataSourceProperties defaultDataSource;

    private List<DataSourceProperties> dataSources;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DataSourceProperties getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(DataSourceProperties defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }

    public List<DataSourceProperties> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSourceProperties> dataSources) {
        this.dataSources = dataSources;
    }

//    public Map<String, DataSourceProperties> determineDataSourceMap() {
//
//        if (CollectionUtils.isEmpty(dataSources)) {
//            return Collections.emptyMap();
//        } else {
//
//            Map<String, DataSourceProperties> dsMap = new HashMap<>();
//            for (DataSourceProperties dataSource : dataSources) {
//
//                String name = dataSource.getName();
//                Assert.hasText(name, "The data source name is not configured");
//                Assert.isTrue(!dsMap.containsKey(name), String.format("Multiple data sources with the same name [%s] are configured", name));
//                dsMap.put(name, dataSource);
//            }
//            return dsMap;
//        }
//    }
}
