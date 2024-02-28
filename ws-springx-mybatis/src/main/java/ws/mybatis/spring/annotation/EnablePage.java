package ws.mybatis.spring.annotation;

import com.github.pagehelper.PageHelper;

import java.lang.annotation.*;

/**
 * 使用示例
 * <code>
 * <pre>
 * &#064Slf4j
 * &#064;EnablePage
 * &#064;RestController
 * public class SomeController {
 *
 *      &#064;@Autowired
 *      private EntityService entityService;
 *      &#064;GetMapping("/query")
 *      public List&lt;Entity&gt; queryAll(&#064;PageNumber  Integer pageNum, &#064;PageSize Integer pageSize)
 *          log.debug("pageNum: {}, pageSize: {}",pageNum,pageSize);
 *          return entityService.listAll();
 *      }
 * }
 * </pre>
 * </code>
 * 发送请求 "GET /query"时，控制器接收到的参数分别为pageNum=1、pageSize=10，并且调用{@link PageHelper#startPage(int, int)}开启分页
 * <p><b>NOTE</b>: 方法上的注解配置将覆盖类上的注解配置
 *
 * @author WindShadow
 * @version 2021-12-26.
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnablePage {

    /**
     * 指定页码参数名，将调用{@link javax.servlet.http.HttpServletRequest#getParameter(String)}方法获取参数
     *
     * @return 分页参数名-页码
     */
    String pageNumberParam() default "page_num";

    /**
     * 设置默认页码
     *
     * @return 默认页码
     */
    int defaultPageNumber() default 1;

    /**
     * 指定页大小参数名，将调用{@link javax.servlet.http.HttpServletRequest#getParameter(String)}方法获取参数
     *
     * @return 分页参数名-页大小
     */
    String pageSizeParam() default "page_size";

    /**
     * 设置默认页大小
     *
     * @return 默认页大小
     */
    int defaultPageSize() default 10;

    /**
     * 分页开关，关闭分页时你依旧可以使用{@link PageNumber}和{@link PageSize}来获取分页参数
     *
     * @return true - 开启分页，false - 关闭分页
     */
    boolean enable() default true;

    /**
     * 当处理器响应被{@code ResponseBody}修饰时，或响应类型为{@code ResponseEntity}时进行增强
     *
     * @return 配置，将分页数据追加到响应对象的属性
     */
    Padding[] paddings() default {};
}
