/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.web.bind;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ws.spring.web.http.RestProxy;
import ws.spring.web.http.DefaultRestProxy;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author WindShadow
 * @version 2025-07-15.
 */
public class RestProxyResolver implements HandlerMethodArgumentResolver {

    private final RestTemplateBuilder restBuilder;

    public RestProxyResolver(RestTemplateBuilder restBuilder) {
        this.restBuilder = Objects.requireNonNull(restBuilder);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        Class<?> type = parameter.getParameterType();
        return RestProxy.class == type;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(request != null, "No HttpServletRequest");
        MultipartHttpServletRequest multipartRequest = webRequest.getNativeRequest(MultipartHttpServletRequest.class);

        ServletServerHttpRequest httpRequest = new ServletServerHttpRequest(request);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpRequest(httpRequest);
        UriComponents components = uriBuilder.build();

        RestProxy.RequestBodyFetcher<?> bodyFetcher;
        if (multipartRequest == null) {
            bodyFetcher = httpRequest::getBody;
        } else {

            bodyFetcher = () -> {

                MultiValueMap<String, HttpEntity<?>> form = new LinkedMultiValueMap<>();

                // 提取并设置通过表单提交的参数
                Set<String> queryParams = components.getQueryParams().keySet();
                Map<String, String[]> parameters = request.getParameterMap();
                parameters.forEach((param, values) -> {

                    if (!queryParams.contains(param)) {
                        for (String value : values) {
                            form.add(param, new HttpEntity<>(value));
                        }
                    }
                });

                // 提取并设置文件
                MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
                for (Map.Entry<String, List<MultipartFile>> entry : fileMap.entrySet()) {

                    String name = entry.getKey();
                    List<MultipartFile> files = entry.getValue();
                    for (MultipartFile file : files) {

                        Resource resource = file.getResource();
                        HttpHeaders headers = multipartRequest.getMultipartHeaders(file.getName());
                        form.add(name, new HttpEntity<>(resource, headers));
                    }
                }
                return form;
            };
        }
        return new DefaultRestProxy(httpRequest.getMethod(), httpRequest.getHeaders(), uriBuilder, bodyFetcher, restBuilder.build());
    }
}
