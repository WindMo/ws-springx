package ws.spring.web.rest.response;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2023-07-06.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public final class SimpleBodyEntity<T> {

    @JsonIgnore
    private String key;

    @JsonIgnore
    private T value;

    @JsonAnyGetter
    public Map<String, T> fetchJsonMap() {

        Assert.state(key != null, "The key of SimpleBodyEntity must not be null");
        return Collections.singletonMap(key, value);
    }

    @JsonAnySetter
    public void putJsonValue(String key, T value) {

        Assert.notNull(key, "The key of SimpleBodyEntity must not be null");
        this.setKey(key);
        this.setValue(value);
    }

    @Override
    public String toString() {
        return key + " = " + value;
    }
}
