/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.Map;


class JacksonSingleEntity<T> implements SingleEntity<T> {

    @JsonIgnore
    private String key;

    @JsonIgnore
    private T value;

    @JsonAnyGetter
    public Map<String, T> fetchJsonMap() {

        if (key == null) throw new IllegalStateException("No set key of SingleEntity");
        return Collections.singletonMap(key, value);
    }

    @JsonAnySetter
    public void putJsonValue(String key, T value) {

        if (this.key != null) throw new IllegalArgumentException("SingleEntity only supports one key");
        replaceKey(key);
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String replaceKey(String key) {

        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("The key of SingleEntity must not be empty/null");
        String oldKey = this.key;
        this.key = key;
        return oldKey;
    }

    @Override
    public T replaceValue(T value) {

        T oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @Override
    public String toString() {
        return this.key + " = " + value;
    }

    @Override
    public T getValue() {
        return value;
    }
}
