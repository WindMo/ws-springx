package ws.spring.web.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import ws.spring.beans.SingleBean;

import java.util.Collections;
import java.util.Map;

/**
 * 用于表示单一结构的对象，支持动态json key，常用于web环境中
 *
 * @author WindShadow
 * @version 2023-07-06.
 * @see ws.spring.beans.SingleBean
 * @see ws.spring.validate.valueextraction.SingleBeanValueExtractor
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public final class SimpleBodyEntity<T> implements SingleBean<T> {

    @JsonIgnore
    private String key;

    @JsonIgnore
    private T value;

    @JsonAnyGetter
    public Map<String, T> fetchJsonMap() {

        Assert.state(key != null, "The key of SimpleBodyEntity must not be null");
        return Collections.singletonMap(key, getValue());
    }

    @JsonAnySetter
    public void putJsonValue(String key, T value) {

        Assert.notNull(key, "The key of SimpleBodyEntity must not be null");
        this.setKey(key);
        this.setValue(value);
    }

    @Override
    public String toString() {
        return getKey() + " = " + getValue();
    }
}
