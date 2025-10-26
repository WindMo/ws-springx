/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2025-07-14.
 */
public interface RestProxy {

    @FunctionalInterface
    interface RequestBodyFetcher<T> {

        T getBody() throws IOException;
    }

    // ~ opt
    // ==================================

    void addHeader(String header, String value);

    void removeHeader(String header);

    default void removeHeaders(List<String> headers) {
        headers.forEach(this::removeHeader);
    }

    default void replaceHeader(String header, String value) {

        removeHeader(header);
        addHeader(header, value);
    }

    default void replaceHeader(String header, List<String> values) {

        removeHeader(header);
        values.forEach(value -> addHeader(header, value));
    }

    void resetHeaders(HttpHeaders headers);

    void removeQueryParam(String name);

    default void removeQueryParams(List<String> names) {
        names.forEach(this::removeQueryParam);
    }

    void replaceQueryParam(String name, String value);

    void replaceQueryParam(String name, List<String> values);

    void resetQueryParams(MultiValueMap<String, String> params);

    void resetQueryParams(Map<String, String> params);

    void replaceMethod(HttpMethod method);

    default void replaceBody(Object body) {
        replaceBody(() -> body);
    }

    void replaceBody(RequestBodyFetcher<Object> bodySupplier);


    void replacePath(@Nullable String path);

    // ~ proxy
    // ==================================

    boolean isSend();

    /**
     * @param origin 如 http://127.0.0.1:8080
     * @param type
     * @return
     * @param <T>
     */
    <T> ResponseEntity<T> proxy(String origin, Class<T> type);

    <T> ResponseEntity<T> proxy(String origin, ParameterizedTypeReference<T> typeRef);

    <T> ResponseEntity<T> proxy(URI uri, Class<T> type);

    <T> ResponseEntity<T> proxy(URI uri, ParameterizedTypeReference<T> typeRef);

    <T> ResponseEntity<T> proxy(String host, int port, Class<T> type);

    <T> ResponseEntity<T> proxy(String host, int port, ParameterizedTypeReference<T> typeRef);

    <T> ResponseEntity<T> proxy(String scheme, String host, int port, Class<T> type);

    /**
     * @param scheme http或https
     * @param host 如 127.0.0.1或localhost
     * @param port 如 8080
     * @param typeRef
     * @return
     * @param <T>
     */
    <T> ResponseEntity<T> proxy(String scheme, String host, int port, ParameterizedTypeReference<T> typeRef);
}