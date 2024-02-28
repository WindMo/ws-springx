package ws.spring.testdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;

/**
 * @author WindShadow
 * @version 2023-07-21.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Person {

    @Email
    private String email;
}
