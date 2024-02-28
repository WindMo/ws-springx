package ws.spring.testdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;

/**
 * @author WindShadow
 * @verion 2020/9/20.
 */

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private String name;
    private Integer age;
    private String desc;

    @Email
    private String email;
}
