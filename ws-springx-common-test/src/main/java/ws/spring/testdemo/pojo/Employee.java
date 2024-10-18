package ws.spring.testdemo.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author WindShadow
 * @version 2024-04-05.
 */

@NoArgsConstructor
@Data
@ToString
public class Employee {

    private Integer empId;
    private String empName;
}
