/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.net;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * TLS工具类
 * <p>附：
 * <ul>
 *     <li>
 *         <a href="https://www.ibm.com/docs/zh/ibm-http-server/9.0.5?topic=options-ssl-cipher-specifications">SSL 密码规范</a>
 *     </li>
 *     <li>
 *         <a href="https://ssl-config.mozilla.org/">SSL/TLS配置生成器</a>
 *     </li>
 * </ul>
 * </p>
 *
 * @author WindShadow
 * @version 2022-08-27.
 * @see SSLContext
 */

@Slf4j
public class TlsAssistants {

    public static final String TLS_V2 = "TLSv1.2";
    public static final String TLS_V3 = "TLSv1.3";
    /**
     * 安全的TLS协议
     */
    private static final Set<String> USUAL_SAFE_TLS_PROTOCOLS;

    /**
     * 安全的TLS加密套件
     */
    private static final Set<String> USUAL_SAFE_TLS_CIPHER_SUITES;

    static {

        USUAL_SAFE_TLS_PROTOCOLS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(TLS_V2, TLS_V3)));
        USUAL_SAFE_TLS_CIPHER_SUITES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
                "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
                "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
                "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
                "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384")));
    }

    public static String[] getUsualSafeTlsProtocols() {
        return USUAL_SAFE_TLS_PROTOCOLS.toArray(new String[0]);
    }

    public static String[] getUsualSafeTlsCipherSuites() {
        return USUAL_SAFE_TLS_CIPHER_SUITES.toArray(new String[0]);
    }


    /**
     * 创建忽略认证服务端证书的tls上下文
     *
     * @return
     */
    public static SSLContext createIgnoreAuthServerTlsContext() {

        return createTlsContextBuilder(builder -> tryLoadTrustMaterial(builder, getSimpleTrustStrategy()));
    }

    /**
     * 创建忽略认证服务端证书的tls上下文，并指定客户端证书
     *
     * @param keyStoreResource 密钥库资源
     * @param keyStorePassword 密钥库密码
     * @param keyPassword      密钥密码
     * @return 实际为TLS的SSLContext
     */
    public static SSLContext createIgnoreAuthServerTlsContext(@NonNull String keyStoreResource, @NonNull char[] keyStorePassword, @NonNull char[] keyPassword) {

        return createTlsContextBuilder(builder -> {

            tryLoadKeyMaterial(builder, keyStoreResource, keyStorePassword, keyPassword);
            tryLoadTrustMaterial(builder, getSimpleTrustStrategy());
        });
    }

    /**
     * 创建单向认证的tls上下文
     *
     * @param trustStoreResource 密钥库资源
     * @param trustStorePassword 密钥库密码
     * @return SSLContext 实际为TLS的SSLContext
     */
    public static SSLContext createUnidirectionalAuthTlsContext(@NonNull String trustStoreResource, @NonNull char[] trustStorePassword) {

        return createTlsContextBuilder(builder -> tryLoadTrustMaterial(builder, trustStoreResource, trustStorePassword));
    }

    /**
     * 创建双向认证的tls上下文
     *
     * @param keyStoreResource 密钥库资源
     * @param keyStorePassword 密钥库密码
     * @param keyPassword      密钥密码
     * @param trustResource    信任密钥库资源
     * @param trustPassword    信任密钥库密码
     * @return SSLContext 实际为TLS的SSLContext
     */
    public static SSLContext createMutualAuthTlsContext(@NonNull String keyStoreResource, @NonNull char[] keyStorePassword, @NonNull char[] keyPassword,
                                                        @NonNull String trustResource, @NonNull char[] trustPassword) {

        return createTlsContextBuilder(builder -> {

            tryLoadKeyMaterial(builder, keyStoreResource, keyStorePassword, keyPassword);
            tryLoadTrustMaterial(builder, trustResource, trustPassword);
        });
    }

    public static void tryLoadKeyMaterial(@NonNull SSLContextBuilder builder, @NonNull String keyStoreResource, @NonNull char[] keyStorePassword, @NonNull char[] keyPassword) {

        Assert.notNull(builder, "The builder must not be null");
        Assert.hasText(keyStoreResource, "The keyStoreResource is invalid");
        Assert.notNull(keyStorePassword, "The keyStorePassword is invalid");
        Assert.notNull(keyPassword, "The keyPassword is invalid");
        try {
            builder.loadKeyMaterial(ResourceUtils.getURL(keyStoreResource), keyStorePassword, keyPassword);
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | UnrecoverableKeyException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void tryLoadTrustMaterial(@NonNull SSLContextBuilder builder, @NonNull String trustResource, @NonNull char[] trustPassword) {

        Assert.notNull(builder, "The builder must not be null");
        Assert.hasText(trustResource, "The trustResource is invalid");
        Assert.notNull(trustPassword, "The trustPassword is invalid");
        try {
            builder.loadTrustMaterial(ResourceUtils.getURL(trustResource), trustPassword);
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void tryLoadTrustMaterial(@NonNull SSLContextBuilder builder, @NonNull TrustStrategy trustStrategy) {

        Assert.notNull(builder, "The builder must not be null");
        Assert.notNull(trustStrategy, "The trustStrategy must not be null");
        try {
            builder.loadTrustMaterial(trustStrategy);
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new IllegalStateException(e);
        }
    }

    public static TrustStrategy getSimpleTrustStrategy() {

        return (chain, authType) -> !ObjectUtils.isEmpty(chain);
    }

    public static HostnameVerifier getSimpleHostnameVerifier() {

        return (s, sslSession) -> {

            try {
                return !ObjectUtils.isEmpty(sslSession.getPeerCertificateChain());
            } catch (SSLPeerUnverifiedException e) {
                if (log.isDebugEnabled()) {
                    log.debug(e.getMessage(), e);
                }
            }
            return false;
        };
    }

    private static SSLContext createTlsContextBuilder(@NonNull Consumer<SSLContextBuilder> builderConsumer) {

        Assert.notNull(builderConsumer, "The builderConsumer must not be null");
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.setProtocol(TLS_V2);
            builder.setSecureRandom(SecureRandom.getInstanceStrong());
            builderConsumer.accept(builder);
            return builder.build();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new IllegalStateException(e);
        }
    }
}
