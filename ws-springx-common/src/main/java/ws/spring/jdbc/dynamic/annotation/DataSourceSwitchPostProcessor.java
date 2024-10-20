package ws.spring.jdbc.dynamic.annotation;

import org.springframework.aop.framework.autoproxy.AbstractBeanFactoryAwareAdvisingPostProcessor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.InitializingBean;
import ws.spring.aop.support.FlexAnnotationMatchingPointcut;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;

import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-04-04.
 */
public class DataSourceSwitchPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessor implements InitializingBean {

    private final SwitchableDataSourceSelector selector;

    public DataSourceSwitchPostProcessor(SwitchableDataSourceSelector selector) {
        this.selector = Objects.requireNonNull(selector);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        this.advisor = new DefaultPointcutAdvisor(
                new FlexAnnotationMatchingPointcut(DataSource.class, false),
                new DataSourceSwitchInterceptor(this.selector));
    }
}