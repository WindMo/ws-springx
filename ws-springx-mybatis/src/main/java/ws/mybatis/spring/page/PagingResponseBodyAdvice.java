package ws.mybatis.spring.page;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import ws.mybatis.spring.annotation.EnablePage;
import ws.mybatis.spring.annotation.Padding;

/**
 * @author WindShadow
 * @version 2022-01-16.
 */

@ControllerAdvice
class PagingResponseBodyAdvice<T> implements ResponseBodyAdvice<T> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return PageInfoHolder.supportsResponseBodyAdvice(returnType);
    }

    @Override
    public T beforeBodyWrite(T body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        EnablePage enablePage = PageInfoHolder.getEnablePageAnnotation(returnType);
        Assert.notNull(enablePage, "This method is not decorated with annotation <" + EnablePage.class.getName() + ">");
        if (enablePage.enable()) {
            PageInfoHolder.updatePageInfo();
            Padding[] paddings = enablePage.paddings();
            Assert.notEmpty(paddings, "Annotation <" + EnablePage.class.getName() + "> on this method [" + returnType.getMethod() + "] does not specify the name of the property to populate the returned data");
            for (Padding padding : paddings) {

                paddingPropertyIfNecessary(body, padding, PageInfoHolder.getCurrentPageInfo());
            }
        }
        return body;
    }

    /**
     * 追加分页信息到指定对象
     *
     * @param obj      目标类型
     * @param padding  padding注解
     * @param pageInfo 当前分页信息
     */
    private void paddingPropertyIfNecessary(Object obj, Padding padding, PageInfo pageInfo) {

        Class<?> expectedType = padding.type();
        if (expectedType.isInstance(obj)) {

            MutablePropertyValues pvs = new MutablePropertyValues();
            String pageNumberProperty = padding.pageNumberProperty();
            if (StringUtils.hasText(pageNumberProperty)) {

                pvs.addPropertyValue(pageNumberProperty, pageInfo.getActualPageNum());
            }
            String pageSizeProperty = padding.pageSizeProperty();
            if (StringUtils.hasText(pageSizeProperty)) {

                pvs.addPropertyValue(pageSizeProperty, pageInfo.getActualPageSize());
            }
            String totalProperty = padding.totalProperty();
            if (StringUtils.hasText(totalProperty)) {

                pvs.addPropertyValue(totalProperty, pageInfo.getActualTotal());
            }

            if (!pvs.isEmpty()) {
                new WebDataBinder(obj).bind(pvs);
            }
        }
    }
}
