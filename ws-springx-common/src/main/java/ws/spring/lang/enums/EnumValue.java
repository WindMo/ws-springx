/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.lang.enums;

import org.springframework.lang.NonNull;

/**
 * 枚举类型实现此接口提供一个value值反映枚举本身，
 * 你可以通过{@linkplain EnumValues}来操作实现了该接口的枚举
 *
 * @author WindShadow
 * @version 2022-02-19.
 * @see EnumValues
 */

public interface EnumValue<T> {

    /**
     * 获取枚举的对应的其它类型的值，该值不能为null，多次调用必须返回同一个值，即满足{@linkplain Object#hashCode() hashCode}和{@linkplain Object#equals(Object) equals比较}的结果相同
     *
     * @return the value, not null
     */
    @NonNull
    T getValue();
}
