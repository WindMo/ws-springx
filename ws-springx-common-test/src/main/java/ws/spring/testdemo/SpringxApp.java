package ws.spring.testdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ws.spring.aop.annotation.EnableMethodPeek;
import ws.spring.text.annotaion.EnableEscape;

/**
 * banner <a href="http://patorjk.com/software/taag/#p=display&h=0&v=0&f=Standard&t=ws-springx">Banner在线生成</a>
 */
@EnableEscape
@EnableMethodPeek
@SpringBootApplication
public class SpringxApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringxApp.class, args);
    }
}
