package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.CollectionUtils;
import ws.spring.jdbc.dynamic.DataSourceFactory;
import ws.spring.jdbc.dynamic.DataSourceInitFailed;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

public class GenericDataSourceFactory implements DataSourceFactory<DataSourceProperties>, ResourceLoaderAware {

    private ResourcePatternResolver resourcePatternResolver;

    @Override
    public DataSource buildDataSource(DataSourceProperties properties) {

        DataSource dataSource = properties.initializeDataSourceBuilder().build();
        initDataSource(dataSource, properties);
        return dataSource;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
    }

    private void initDataSource(DataSource dataSource, DataSourceProperties properties) {

        try {
            runScript(dataSource, properties.getSchema());
        } catch (Exception e) {
            throw new DataSourceInitFailed(String.format("Data source with initial alias [%s] failed. Failed to initialize it's schema", properties.getName()), e);
        }
        try {
            runScript(dataSource, properties.getData());
        } catch (Exception e) {
            throw new DataSourceInitFailed(String.format("Data source with initial alias [%s] failed. Failed to initialize it's data", properties.getName()), e);
        }
    }

    private void runScript(DataSource dataSource, List<String> locations) throws IOException {

        if (CollectionUtils.isEmpty(locations)) return;
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        for (String location : locations) {
            for (Resource resource : resourcePatternResolver.getResources(location)) {
                populator.addScript(resource);
            }
        }
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}
