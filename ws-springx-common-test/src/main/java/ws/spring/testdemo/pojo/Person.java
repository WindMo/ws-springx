package ws.spring.testdemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
    @NotBlank
    private String email;
}
