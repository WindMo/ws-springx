/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.util;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import ws.spring.net.TlsAssistants;

import javax.net.ssl.SSLContext;

/**
 * http请求客户端工具类
 *
 * @author WindShadow
 * @version 2022-08-28.
 * @see RestTemplate
 * @see HttpComponentsClientHttpRequestFactory
 * @see HttpClient
 */

public class HttpClientAssistants {

    /**
     * 忽略认证服务端证书的http客户端请求工厂
     *
     * @return
     */
    public static HttpComponentsClientHttpRequestFactory getIgnoreAuthServerClientHttpRequestFactory() {

        return getIgnoreHostnameValidityClientHttpRequestFactory(
                TlsAssistants.createIgnoreAuthServerTlsContext());
    }

    /**
     * 忽略认证服务端证书的http客户端请求工厂，并指定客户端证书
     *
     * @param keyStoreResource 密钥库资源
     * @param keyStorePassword 密钥库密码
     * @param keyPassword      密钥密码
     * @return
     */
    public static HttpComponentsClientHttpRequestFactory getIgnoreAuthServerClientHttpRequestFactory(@NonNull String keyStoreResource, @NonNull char[] keyStorePassword, @NonNull char[] keyPassword) {

        return getIgnoreHostnameValidityClientHttpRequestFactory(
                TlsAssistants.createIgnoreAuthServerTlsContext(keyStoreResource, keyStorePassword, keyPassword));
    }

    /**
     * 获取单向认证http客户端请求工厂
     *
     * @param trustStoreResource 密钥库资源
     * @param trustStorePassword 密钥库密码
     * @return 单向认证http客户端请求工厂
     */
    public static HttpComponentsClientHttpRequestFactory getUnidirectionalAuthClientHttpRequestFactory(@NonNull String trustStoreResource, @NonNull char[] trustStorePassword) {

        return getIgnoreHostnameValidityClientHttpRequestFactory(
                TlsAssistants.createUnidirectionalAuthTlsContext(trustStoreResource, trustStorePassword));
    }

    /**
     * 获取双向认证http客户端请求工厂
     *
     * @param keyStoreResource 密钥库资源
     * @param keyStorePassword 密钥库密码
     * @param keyPassword      密钥密码
     * @param trustResource    信任密钥库资源
     * @param trustPassword    信任密钥库密码
     * @return 双向认证http客户端请求工厂
     */
    public static HttpComponentsClientHttpRequestFactory getMutualAuthClientHttpRequestFactory(@NonNull String keyStoreResource, @NonNull char[] keyStorePassword, @NonNull char[] keyPassword,
                                                                                 @NonNull String trustResource, @NonNull char[] trustPassword) {
        return getIgnoreHostnameValidityClientHttpRequestFactory(
                TlsAssistants.createMutualAuthTlsContext(keyStoreResource, keyStorePassword, keyPassword, trustResource, trustPassword));
    }

    public static HttpComponentsClientHttpRequestFactory getIgnoreHostnameValidityClientHttpRequestFactory(@NonNull SSLContext sslContext) {

        return new HttpComponentsClientHttpRequestFactory(getIgnoreHostnameValidityHttpClient(sslContext));
    }

    /**
     * 获取忽略域名校验的http客户端
     *
     * @param sslContext ssl上下文，开发者最好使用tls的安全层协议
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getIgnoreHostnameValidityHttpClient(@NonNull SSLContext sslContext) {

        Assert.notNull(sslContext, "The sslContext must not be null");
        return HttpClientBuilder.create()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(
                        sslContext,
                        TlsAssistants.getUsualSafeTlsProtocols(),
                        TlsAssistants.getUsualSafeTlsCipherSuites(),
                        TlsAssistants.getSimpleHostnameVerifier()))
                .build();
    }
}
