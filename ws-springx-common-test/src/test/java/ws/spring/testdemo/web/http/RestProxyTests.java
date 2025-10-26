/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.http;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.UriComponentsBuilder;
import ws.spring.constant.NetworkConstants;
import ws.spring.testdemo.SpringxAppWebTests;
import ws.spring.testdemo.assistant.TestHook;
import ws.spring.testdemo.web.controller.HttpProxyController;
import ws.spring.web.http.RestProxy;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author WindShadow
 * @version 2025-07-17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestProxyTests extends SpringxAppWebTests {

    private static final HttpMethod[] METHODS = {HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE};

    private final RandomValuePropertySource source = new RandomValuePropertySource();
    private final Random random = new Random();

    @LocalServerPort
    private int localServerPort;
    @Autowired
    private TestHook hook;

    private final String hookFacade = "Facade";
    private final String hookReal = "Real";

    private String origin;

    private HttpMethod method = null;
    private HttpHeaders headers = new HttpHeaders();
    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private byte[] body = null;
    private String realData = null;

    private String randomStr() {
        return String.valueOf(source.getProperty("random.value"));
    }

    @BeforeEach
    void initProxyOrigin() {
        this.origin = "http://" + NetworkConstants.LOCALHOST_IP_V4 + ":" + localServerPort;
    }

    private void initRequestData() {

        headers.clear();
        params.clear();

        method = METHODS[random.nextInt(METHODS.length)];

        for (int i = 0, size = random.nextInt(10) + 1; i < size; i++) {

            headers.add(randomStr(), randomStr());
            params.add(randomStr(), randomStr());
        }
        body = randomStr().getBytes(StandardCharsets.UTF_8);
        realData = randomStr();
        headers.set(HttpProxyController.REAL_DATA, realData);
    }

    @AfterEach
    void clearHook() {

        hook.remmoveHook(hookFacade);
        hook.remmoveHook(hookReal);
    }

    @Test
    void proxyTest() {

        initRequestData();

        headers.set(HttpProxyController.HOOK_KEY, hookFacade);
        headers.set(HttpProxyController.PROXY_ORIGIN, origin);

        hook.addHook(hookFacade, obj -> {

            Assertions.assertInstanceOf(RestProxy.class, obj);
            RestProxy proxy = (RestProxy) obj;
            initRequestData();
            proxy.replaceMethod(method);
            proxy.resetHeaders(headers);
            proxy.resetQueryParams(params);
            proxy.replaceBody(body);
            proxy.replacePath("/bind/extend/http-proxy/real");

            proxy.replaceHeader(HttpProxyController.HOOK_KEY, hookReal);
        });

        hook.addHook(hookReal, obj -> {

            Assertions.assertInstanceOf(HttpServletRequest.class, obj);
            HttpServletRequest request = (HttpServletRequest) obj;
            ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);
            HttpHeaders actualHeaders = httpRequest.getHeaders();
            headers.forEach((header, values) -> Assertions.assertEquals(values, actualHeaders.get(header)));
            MultiValueMap<String, String> actualParams = UriComponentsBuilder.fromUri(httpRequest.getURI())
                    .build()
                    .getQueryParams();
            Assertions.assertEquals(params, actualParams);
            byte[] actualBody;
            try {
                actualBody = StreamUtils.copyToByteArray(httpRequest.getBody());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            Assertions.assertArrayEquals(body, actualBody);
        });

        String realData = request(MockMvcRequestBuilders.request(method, "/bind/extend/http-proxy/facade")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .params(params)
                .headers(headers)
                .content(body));
        Assertions.assertEquals(this.realData, realData);
    }

    @Test
    void uploadProxyTest() {

        hook.addHook(hookFacade, obj -> {

            Assertions.assertInstanceOf(RestProxy.class, obj);
            RestProxy proxy = (RestProxy) obj;
            proxy.replacePath("/bind/extend/http-proxy/upload");
        });

        String downloadFilename = randomStr();
        byte[] fileContent = randomStr().getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile("file", "random-file.pdf", MediaType.APPLICATION_PDF_VALUE, fileContent);
        byte[] downloadContent = requestBytes(MockMvcRequestBuilders.multipart(HttpMethod.POST, "/bind/extend/http-proxy/facade")
                .file(file)
                .header(HttpProxyController.HOOK_KEY, hookFacade)
                .header(HttpProxyController.PROXY_ORIGIN, origin)
                .header(HttpProxyController.PROXY_ORIGIN_DOWNLOAD, true)
                .queryParam("aaa", "123")
                .param("downloadFilename", downloadFilename));
        Assertions.assertArrayEquals(fileContent, downloadContent);
    }
}
