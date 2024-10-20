package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */
@Validated
@ConfigurationProperties("spring.ext.dynamic-datasource")
public class DynamicDataSourceProperties {

    private boolean enabled = false;

//    @NotNull
    @NestedConfigurationProperty
    private DataSourceProperties defaultDataSource;

    private Set<@Valid NamedDataSourceProperties> dataSources;

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

    public Set<NamedDataSourceProperties> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Set<NamedDataSourceProperties> dataSources) {
        this.dataSources = dataSources;
    }

    public Map<String, DataSourceProperties> fetchDataSourceMap() {

        if (dataSources == null) {
            return Collections.emptyMap();
        } else {

            Map<String, DataSourceProperties> dataSourceMap = new HashMap<>();
            dataSources.forEach(properties -> dataSourceMap.put(properties.getName(), properties));
            return dataSourceMap;
        }
    }
}
