package ws.spring.mybatis.mapper;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author WindShadow
 * @version 2025-03-30.
 */
@Repeatable(ImportMapper.List.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MapperRegistrar.class)
public @interface ImportMapper {

    Class<?> mapper();

    String location() default "";

    String sqlSessionFactoryRef() default "";

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Import(MapperRegistrar.class)
    @interface List {

        ImportMapper[] value();
    }
}
