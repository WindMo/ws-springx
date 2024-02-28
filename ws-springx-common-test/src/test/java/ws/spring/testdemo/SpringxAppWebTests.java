package ws.spring.testdemo;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ws.spring.testdemo.util.JacksonUtils;
import ws.spring.testdemo.web.rest.GlobalRest;
import ws.spring.web.rest.response.RestResponse;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author WindShadow
 * @version 2023-06-06.
 */

@AutoConfigureMockMvc
public class SpringxAppWebTests extends SpringxAppTests {

    @Autowired
    protected MockMvc mvc;

    protected final <T> T request(RequestBuilder builder) {

        return request(builder, status().is2xxSuccessful());
    }

    protected final <T> T request(RequestBuilder builder, ResultMatcher matcher) {

        String json = requestString(builder, matcher);
        RestResponse<T> restResponse = JacksonUtils.parse(json, new TypeReference<RestResponse<T>>() {});
        Assertions.assertEquals(GlobalRest.SUCCESS.getCode(), restResponse.getCode());
        return restResponse.getData();
    }

    protected final String requestString(RequestBuilder builder) {

        return requestString(builder, status().is2xxSuccessful());
    }

    protected final String requestString(RequestBuilder builder, ResultMatcher matcher) {

        return new String(requestBytes(builder, matcher), StandardCharsets.UTF_8);
    }

    protected final byte[] requestBytes(RequestBuilder builder) {

        return requestBytes(builder, status().is2xxSuccessful());
    }

    protected final byte[] requestBytes(RequestBuilder builder, ResultMatcher matcher) {

        try {

            if (builder instanceof MockHttpServletRequestBuilder) {

                MockHttpServletRequestBuilder mockBuilder = (MockHttpServletRequestBuilder) builder;
                mockBuilder.characterEncoding(StandardCharsets.UTF_8.name());
                mockBuilder.contentType(MediaType.APPLICATION_JSON);
            }
            return mvc.perform(builder)
                    .andExpect(matcher)
                    .andReturn()
                    .getResponse()
                    .getContentAsByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
