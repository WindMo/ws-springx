package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */
@ConfigurationProperties("spring.ext.dynamic-datasource.selector")
public class DataSourceSelectorProperties {

    private boolean selectorInheritable = true;

    public boolean isSelectorInheritable() {
        return selectorInheritable;
    }

    public void setSelectorInheritable(boolean selectorInheritable) {
        this.selectorInheritable = selectorInheritable;
    }
}
