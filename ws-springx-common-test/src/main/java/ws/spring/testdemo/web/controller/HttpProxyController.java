/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.spring.testdemo.assistant.TestHook;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.http.RestProxy;
import ws.spring.web.rest.response.RestResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author WindShadow
 * @version 2025-07-17.
 */

@RestController
@RequestMapping("/bind/extend/http-proxy")
public class HttpProxyController {

    public static final String HOOK_KEY = "X-Hook-Key";
    public static final String PROXY_ORIGIN = "X-Proxy-Origin";
    public static final String PROXY_ORIGIN_DOWNLOAD = "X-Proxy-Download";
    public static final String REAL_DATA = "X-Real-Data";

    @Autowired
    private TestHook hook;

    @RequestMapping("/facade")
    public ResponseEntity<?> facade(@RequestHeader(value = HOOK_KEY, required = false) String hookKey,
                                    @RequestHeader(PROXY_ORIGIN) String proxyOrigin,
                                    @RequestHeader(value = PROXY_ORIGIN_DOWNLOAD, required = false, defaultValue = "false") Boolean download,
                                    RestProxy proxy) {

        if (hookKey != null) {
            hook.callHookIfExist(hookKey, proxy);
        }
        return download ? proxy.proxy(proxyOrigin, Resource.class) : proxy.proxy(proxyOrigin, Object.class);
    }

    @RequestMapping("/real")
    public RestResponse<String> real(@RequestHeader(HOOK_KEY) String hookKey,
                                     @RequestHeader(REAL_DATA) String realData,
                                     HttpServletRequest request) {

        hook.callHookIfExist(hookKey, request);
        return GlobalRest.SUCCESS.of(realData);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> uploadFile(@RequestPart("file") MultipartFile file,
                                               @RequestParam("downloadFilename") String downloadFilename) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(downloadFilename, StandardCharsets.UTF_8)
                .build());
        Resource resource = new ByteArrayResource(StreamUtils.copyToByteArray(file.getInputStream()));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
