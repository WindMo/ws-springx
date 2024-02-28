package ws.spring.web.bind.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 通常解决表单提交带有不同前缀相同后缀的参数绑定到pojo的痛点
 * <p>
 * 假设有如下表单提交：
 *  <table cellspacing=10>
 *      <thead>
 *         <tr>
 *            <th>参数名</th>
 *            <th>参数值</th>
 *         </tr>
 *         <tr>
 *             <td>user.name</td><td>WindShadow</td>
 *         </tr>
 *         <tr>
 *              <td>user.desc</td><td>一名软件工程师</td>
 *         </tr>
 *         <tr>
 *              <td>city.name</td><td>北京</td>
 *         </tr>
 *         <tr>
 *              <td>city.desc</td><td>中国首都</td>
 *         </tr>
 *      </thead>
 *  </table>
 *  你可以使用 FormModel 注解进行如下参数映射
 *
 *  <pre class="code">
 *  &#64;GetMapping("/from-model")
 *  public String formModel(&#64;FormModel("user") User u, &#64;FormModel("city") City c) {
 *      // ...
 *  }</pre>
 * <p>
 * 实际 FormModel 是处理来自{@link javax.servlet.http.HttpServletRequest#getParameter(String)}的参数键值对，
 * 如上述示例代码，它会将从 Request 对象中获取 "user.name" 的参数值绑定到 User 对象的 name 属性
 * <p><b>NOTE</b>：FormModel 不支持基础类型与其包装类的绑定以及String类型，因为使用{@link org.springframework.web.bind.annotation.RequestParam}就可以达到相同的效果
 *
 * @author WindShadow
 * @version 2022-06-25.
 * @see javax.servlet.http.HttpServletRequest
 * @see org.springframework.web.bind.WebDataBinder
 */

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormModel {

    /**
     * AliasFor {@link #prefix}
     */
    @AliasFor("prefix")
    String value() default "";

    /**
     * 参数前缀，为空时取方法参数名
     */
    @AliasFor("value")
    String prefix() default "";

    String separator() default ".";

    boolean required() default true;
}
