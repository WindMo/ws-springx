/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ws.spring.beans.SingleBean;

/**
 * @author WindShadow
 * @version 2025-05-30.
 */

@JsonDeserialize(as = JacksonSingleEntity.class)
public interface SingleEntity<E> extends SingleBean<E> {

    String getKey();

    /**
     * @param key new key
     * @return old key
     */
    String replaceKey(String key);

    /**
     * @param value new value
     * @return old value
     */
    E replaceValue(E value);

    static <T> SingleEntity<T> of(String key, T value) {

        JacksonSingleEntity<T> entity = new JacksonSingleEntity<>();
        entity.putJsonValue(key, value);
        return entity;
    }
}
