package ws.spring.mybatis.mapper;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * 另一种mybatis mapper工厂bean，可根据mybatis配置灵活注册mapper接口和解析对应xml资源，其中xml资源是可选的
 *
 * @author WindShadow
 * @version 2025-03-29.
 */
public class EnhancedMapperFactory<T> extends SqlSessionDaoSupport implements FactoryBean<T> {

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    private Class<T> mapperInterface;

    @Nullable
    private String mapperLocation;

    public EnhancedMapperFactory() {
    }

    public EnhancedMapperFactory(Class<T> mapperInterface) {
        this(mapperInterface, null);
    }

    public EnhancedMapperFactory(Class<T> mapperInterface, @Nullable String mapperLocation) {
        this.mapperInterface = mapperInterface;
        this.mapperLocation = mapperLocation;
    }

    public Class<T> getMapperInterface() {
        return mapperInterface;
    }

    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Nullable
    public String getMapperLocation() {
        return mapperLocation;
    }

    public void setMapperLocation(@Nullable String mapperLocation) {
        this.mapperLocation = mapperLocation;
    }

    @Override
    protected void checkDaoConfig() {

        super.checkDaoConfig();
        Assert.notNull(this.mapperInterface, "Property 'mapperInterface' is required");
        Configuration configuration = this.getSqlSession().getConfiguration();
        if (!configuration.hasMapper(this.mapperInterface)) {
            try {
                configuration.addMapper(this.mapperInterface);
            } catch (Exception e) {
                this.logger.error("Error while adding the mapper '" + this.mapperInterface + "' to configuration.", e);
                throw new IllegalArgumentException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }
        if (this.mapperLocation == null) return;
        final String namespace = "namespace:" + this.mapperInterface.getName();
        if (!configuration.isResourceLoaded(namespace)) {

            try {

                Resource location = resolveMapperLocationResource();
                String resourceName = location.toString();
                new XMLMapperBuilder(location.getInputStream(), configuration, resourceName, configuration.getSqlFragments()).parse();
            } catch (IOException e) {
                this.logger.error("Error while adding the mapper.xml '" + this.mapperLocation + "' to configuration.", e);
                throw new IllegalStateException(e);
            } finally {
                ErrorContext.instance().reset();
            }
        }

    }

    @Override
    public T getObject() throws Exception {
        return getSqlSession().getMapper(this.mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return this.mapperInterface;
    }

    @Nullable
    private Resource resolveMapperLocationResource() {

        if (this.mapperLocation == null) return null;
        try {
            Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(this.mapperLocation);
            Assert.isTrue(resources.length != 0, () -> String.format("The resource of the mapper.xml location '%s' does not exist", this.mapperLocation));
            Assert.isTrue(resources.length == 1, () -> String.format("Resolve the mapper.xml '%s' location to discover multiple resources", this.mapperLocation));
            return resources[0];
        } catch (IOException e) {
            this.logger.error(String.format("Error while resolving the mapper.xml location '%s'.", this.mapperLocation), e);
            throw new IllegalStateException(e);
        }
    }
}
