package ws.mybatis.spring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2021-12-26.
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Person {

    private Long id;
    private String name;
    private Integer age;
    private String sex;
    private String phone;
}
