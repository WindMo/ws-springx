/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JacksonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String toJson(T obj) {

        return toJsonWithMapper(OBJECT_MAPPER, obj);
    }

    public static <T> T parse(String json, Class<T> cla) {

        return parseWithMapper(OBJECT_MAPPER, json, cla);
    }

    public static <T> T parse(String json, TypeReference<T> trf) {

        return parseWithMapper(OBJECT_MAPPER, json, trf);
    }

    public static <T> String toJsonWithMapper(ObjectMapper mapper, T obj) {

        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseWithMapper(ObjectMapper mapper, String json, Class<T> cla) {

        try {
            return mapper.readValue(json, cla);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseWithMapper(ObjectMapper mapper, String json, TypeReference<T> trf) {

        try {
            return mapper.readValue(json, trf);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
