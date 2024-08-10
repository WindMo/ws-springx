package ws.spring.testdemo.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2024-10-15.
 */
@NoArgsConstructor
@Data
@ToString
public class FakeUser {

    private String name;
    private boolean enabled;

    public int getAge() {
        return 0;
    }

    public void setEmail(String email) {

    }

    public boolean isFake() {
        return true;
    }

    public String fetchAddress() {
        return null;
    }
}
