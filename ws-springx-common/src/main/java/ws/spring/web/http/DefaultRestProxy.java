/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2025-07-15.
 */
public class DefaultRestProxy extends AbstractRestProxy {

    private final RestOperations rest;

    public DefaultRestProxy(HttpMethod method, HttpHeaders headers, URI uri, @Nullable RequestBodyFetcher<?> bodySupplier, RestOperations rest) {
        super(method, headers, uri, bodySupplier);
        this.rest = Objects.requireNonNull(rest);
    }

    public DefaultRestProxy(HttpMethod method, HttpHeaders headers, UriComponentsBuilder builder, @Nullable RequestBodyFetcher<?> bodySupplier, RestOperations rest) {
        super(method, headers, builder, bodySupplier);
        this.rest = Objects.requireNonNull(rest);
    }

    @Override
    protected <T> ResponseEntity<T> doProxy(RequestEntity<Object> entity, Class<T> type) {
        return rest.exchange(entity, type);
    }

    @Override
    protected <T> ResponseEntity<T> doProxy(RequestEntity<Object> entity, ParameterizedTypeReference<T> typeRef) {
        return rest.exchange(entity, typeRef);
    }
}
