package ws.spring.jdbc.dynamic.autoconfigure;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */
public class NamedDataSourceProperties extends DataSourceProperties {

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedDataSourceProperties)) return false;
        NamedDataSourceProperties that = (NamedDataSourceProperties) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}