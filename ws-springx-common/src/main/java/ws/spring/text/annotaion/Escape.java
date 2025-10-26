/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.text.annotaion;

import ws.spring.text.Escaper;

import java.lang.annotation.*;

/**
 * 转义方法参数，通常你应该在最终声明方法的类中去使用Escape，
 * 如你应该在接口的实现类的方法上而不是接口的方法上，子类覆写父类方法时，应该在子类方法上使用
 *
 * @author WindShadow
 * @version 2023-05-01.
 */

@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Escape {

    Class<? extends Escaper> value();
}
