/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author WindShadow
 * @version 2024-03-30.
 */
public class SecurityUtils {

    private static final byte[] COMMON_PLAIN_BYTES = SecurityUtils.class.getName().getBytes(StandardCharsets.UTF_8);

    static {
        addProviderIfNecessary(BouncyCastleProvider.PROVIDER_NAME, BouncyCastleProvider::new);
    }

    public static boolean matchKeys(String algorithm, PublicKey publicKey, PrivateKey privateKey) {

        try {
            // 公钥加密
            Cipher encryptCipher = Cipher.getInstance(algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = encryptCipher.doFinal(COMMON_PLAIN_BYTES);

            // 私钥解密
            Cipher decryptCipher = Cipher.getInstance(algorithm);
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
            return Arrays.equals(COMMON_PLAIN_BYTES, decryptedBytes);
        } catch (NoSuchPaddingException e) {
            // never call
            throw new UnsupportedOperationException(e);
        } catch (IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new IllegalStateException("Unable to match", e);
        }
    }

    public static PublicKey readPublicKey(String location, @Nullable char[] password) throws IOException {
        return readPublicKey(ResourceUtils.getURL(location), password);
    }

    public static PublicKey readPublicKey(URL url, @Nullable char[] password) throws IOException {
        return readPublicKey(new UrlResource(url), password);
    }

    public static PublicKey readPublicKey(Resource resource, @Nullable char[] password) throws IOException {
        return readPublicKey(resource.getInputStream(), password);
    }

    public static PublicKey readPublicKey(InputStream in, @Nullable char[] password) throws IOException {

        // 通常此处不会抛出IllegalArgumentException异常
        return Optional.of(readKeyPair(in, password))
                .map(KeyPair::getPublic)
                .orElseThrow(() -> new IllegalArgumentException("Not found a PublicKey"));
    }

    public static PrivateKey readPrivateKey(String location, @Nullable char[] password) throws IOException {
        return readPrivateKey(ResourceUtils.getURL(location), password);
    }

    public static PrivateKey readPrivateKey(URL url, @Nullable char[] password) throws IOException {
        return readPrivateKey(new UrlResource(url), password);
    }

    public static PrivateKey readPrivateKey(Resource resource, @Nullable char[] password) throws IOException {
        return readPrivateKey(resource.getInputStream(), password);
    }

    public static PrivateKey readPrivateKey(InputStream in, @Nullable char[] password) throws IOException {

        // 通常此处不会抛出IllegalArgumentException异常
        return Optional.of(readKeyPair(in, password))
                .map(KeyPair::getPrivate)
                .orElseThrow(() -> new IllegalArgumentException("Not found a PrivateKey"));
    }

    public static KeyPair readKeyPair(String location, @Nullable char[] password) throws IOException {

        return readKeyPair(ResourceUtils.getURL(location), password);
    }

    public static KeyPair readKeyPair(URL url, @Nullable char[] password) throws IOException {
        return readKeyPair(new UrlResource(url), password);
    }

    public static KeyPair readKeyPair(Resource resource, @Nullable char[] password) throws IOException {

        return readKeyPair(resource.getInputStream(), password);
    }

    public static KeyPair readKeyPair(InputStream in, @Nullable char[] password) throws IOException {

        PEMKeyPair pemKeyPair = parsePEMKeyPair(in, password);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        return converter.getKeyPair(pemKeyPair);
    }

    private static PEMKeyPair parsePEMKeyPair(InputStream in, @Nullable char[] password) throws IOException {

        PEMKeyPair pemKeyPair;
        try (PEMParser pemParser = new PEMParser(new InputStreamReader(in))) {

            Object pem = pemParser.readObject();
            if (pem instanceof PEMKeyPair) {

                Assert.isTrue(password == null, "Cannot decrypt an unencrypted key");
                pemKeyPair = (PEMKeyPair) pem;
            } else if (pem instanceof PEMEncryptedKeyPair) {

                // 密钥加密则解密
                Assert.notNull(password, "The key is encrypted but no password is provided");
                PEMDecryptorProvider dp = new JcePEMDecryptorProviderBuilder()
                        .setProvider(BouncyCastleProvider.PROVIDER_NAME)
                        .build(password);
                pemKeyPair = ((PEMEncryptedKeyPair) pem).decryptKeyPair(dp);

            } else {
                throw new UnsupportedOperationException("The pem key is unknown content");
            }
        }
        return pemKeyPair;
    }

    public static void addProviderIfNecessary(String name, Supplier<Provider> supplier) {

        Objects.requireNonNull(name);
        Objects.requireNonNull(supplier);
        if (Security.getProvider(name) == null) {
            Security.addProvider(Objects.requireNonNull(supplier.get(), "Invalid Provider is provided"));
        }
    }
}
