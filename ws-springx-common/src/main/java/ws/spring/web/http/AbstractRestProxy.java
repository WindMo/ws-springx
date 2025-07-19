package ws.spring.web.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2025-07-15.
 */
public abstract class AbstractRestProxy implements RestProxy {

    private boolean send = false;
    private HttpMethod method;
    private final HttpHeaders headers;
    private final UriComponentsBuilder builder;
    
    @Nullable
    private RequestBodyFetcher<?> bodyRequestBodyFetcher;

    protected AbstractRestProxy(HttpMethod method, HttpHeaders headers, URI uri, @Nullable RequestBodyFetcher<?> bodyRequestBodyFetcher) {
        this(method, headers, UriComponentsBuilder.fromUri(uri), bodyRequestBodyFetcher);
    }
    
    protected AbstractRestProxy(HttpMethod method, HttpHeaders headers, UriComponentsBuilder builder, @Nullable RequestBodyFetcher<?> bodyRequestBodyFetcher) {

        this.method = Objects.requireNonNull(method);
        this.headers = Objects.requireNonNull(headers);
        this.builder = Objects.requireNonNull(builder);
        this.bodyRequestBodyFetcher = bodyRequestBodyFetcher;
    }

    @Override
    public void addHeader(String header, String value) {

        checkState();
        headers.add(header, value);
    }

    @Override
    public void removeHeader(String header) {

        checkState();
        headers.remove(header);
    }

    @Override
    public void replaceHeader(String header, String value) {

        checkState();
        headers.set(header, value);
    }

    @Override
    public void replaceHeader(String header, List<String> values) {

        checkState();
        headers.replace(header, values);
    }

    @Override
    public void resetHeaders(HttpHeaders headers) {

        checkState();
        this.headers.clear();
        this.headers.putAll(headers);
    }

    @Override
    public void removeQueryParam(String name) {

        checkState();
        builder.replaceQuery(name);
    }

    @Override
    public void replaceQueryParam(String name, String value) {

        checkState();
        builder.replaceQueryParam(name, value);
    }

    @Override
    public void replaceQueryParam(String name, List<String> values) {

        checkState();
        builder.replaceQueryParam(name, values);
    }

    @Override
    public void resetQueryParams(MultiValueMap<String, String> params) {

        checkState();
        builder.replaceQueryParams(params);
    }

    @Override
    public void resetQueryParams(Map<String, String> params) {

        checkState();
        builder.replaceQueryParams(null);
        params.forEach(builder::queryParam);
    }

    @Override
    public void replaceMethod(HttpMethod method) {

        checkState();
        this.method = Objects.requireNonNull(method);
    }

    @Override
    public void replaceBody(RequestBodyFetcher<Object> bodyRequestBodyFetcher) {

        checkState();
        this.bodyRequestBodyFetcher = bodyRequestBodyFetcher;
    }

    @Override
    public void replacePath(String path) {

        checkState();
        builder.replacePath(path);
    }

    @Override
    public final boolean isSend() {
        return send;
    }

    @Override
    public final <T> ResponseEntity<T> proxy(String origin, Class<T> type) {

        preSend(origin);
        return send(type);
    }

    @Override
    public final <T> ResponseEntity<T> proxy(String origin, ParameterizedTypeReference<T> typeRef) {

        preSend(origin);
        return send(typeRef);
    }

    @Override
    public final <T> ResponseEntity<T> proxy(URI uri, Class<T> type) {

        preSend(uri);
        return send(type);
    }

    @Override
    public final <T> ResponseEntity<T> proxy(URI uri, ParameterizedTypeReference<T> typeRef) {

        preSend(uri);
        return send(typeRef);
    }

    @Override
    public <T> ResponseEntity<T> proxy(String host, int port, Class<T> type) {

        Assert.hasText(host, "The host must not be empty/null");
        return internalProxy(null, host, port, type);
    }

    @Override
    public <T> ResponseEntity<T> proxy(String host, int port, ParameterizedTypeReference<T> typeRef) {

        Assert.hasText(host, "The host must not be empty/null");
        return internalProxy(null, host, port, typeRef);
    }

    @Override
    public final <T> ResponseEntity<T> proxy(String scheme, String host, int port, Class<T> type) {

        Assert.hasText(scheme, "The scheme must not be empty/null");
        Assert.hasText(host, "The host must not be empty/null");
        return internalProxy(scheme, host, port, type);
    }

    @Override
    public final <T> ResponseEntity<T> proxy(String scheme, String host, int port, ParameterizedTypeReference<T> typeRef) {

        Assert.hasText(scheme, "The scheme must not be empty/null");
        Assert.hasText(host, "The host must not be empty/null");
        return internalProxy(scheme, host, port, typeRef);
    }

    private <T> ResponseEntity<T> internalProxy(@Nullable String scheme, @Nullable String host, @Nullable Integer port, Class<T> type) {

        preSend(scheme, host, port);
        return send(type);
    }

    private <T> ResponseEntity<T> internalProxy(@Nullable String scheme, @Nullable String host, @Nullable Integer port, ParameterizedTypeReference<T> typeRef) {

        preSend(scheme, host, port);
        return send(typeRef);
    }

    private void preSend(String origin) {

        Assert.hasText(origin, "The origin must not be empty/null");
        checkState();

        if (origin.startsWith("http://") || origin.startsWith("https://")) {
            URL url;
            try {
                url = new URL(origin);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(String.format("[%s] is not a valid origin", origin));
            }
            String scheme = url.getProtocol();
            String host = url.getHost();
            Integer port = url.getPort() == -1 ? null : url.getPort();
            preSend(scheme, host, port);
        } else {
            throw new IllegalArgumentException(String.format("[%s] is not a valid origin", origin));
        }
    }

    private void preSend(URI uri) {

        Assert.notNull(uri, "The uri must not be null");
        checkState();
        builder.uri(uri);
    }

    private void preSend(@Nullable String scheme, @Nullable String host, @Nullable Integer port) {

        checkState();
        if (scheme != null) {
            builder.scheme(scheme);
        }
        if (host != null) {
            builder.host(host);
        }
        if (port != null) {
            builder.port(port);
        }
    }

    private <T> ResponseEntity<T> send(Class<T> type) {

        checkState();
        try {
            return doProxy(buildRequestEntity(), type);
        } finally {
            this.send = true;
        }
    }

    private <T> ResponseEntity<T> send(ParameterizedTypeReference<T> typeRef) {

        checkState();
        try {
            return doProxy(buildRequestEntity(), typeRef);
        } finally {
            this.send = true;
        }
    }
    
    private RequestEntity<Object> buildRequestEntity() {

        Object body;
        try {
            body = bodyRequestBodyFetcher == null ? null : bodyRequestBodyFetcher.getBody();
        } catch (IOException e) {
            throw new IllegalStateException("Error while fetching source request body of HttpProxy", e);
        }
        if (body instanceof InputStream) {
            body = new InputStreamResource((InputStream) body);
        }
        return new RequestEntity<>(body, headers, method, builder.build().toUri());
    }

    private void checkState() {
        Assert.state(!isSend(), "The proxy is sent");
    }

    protected abstract <T> ResponseEntity<T> doProxy(RequestEntity<Object> entity, Class<T> type);

    protected abstract <T> ResponseEntity<T> doProxy(RequestEntity<Object> entity, ParameterizedTypeReference<T> typeRef);
}
