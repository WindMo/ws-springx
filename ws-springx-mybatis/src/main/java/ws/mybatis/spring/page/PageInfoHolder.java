package ws.mybatis.spring.page;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import ws.mybatis.spring.annotation.EnablePage;
import ws.mybatis.spring.annotation.PageNumber;
import ws.mybatis.spring.annotation.PageSize;

import java.util.function.Function;

/**
 * @author WindShadow
 * @version 2022-01-08.
 */

class PageInfoHolder {

    private static final String PAGE_INFO = PageInfoHolder.class.getName() + ".PAGE_INFO";

    static boolean hasEnablePageAnnotation(MethodParameter parameter) {

        return getEnablePageAnnotation(parameter) != null;
    }

    @Nullable
    static EnablePage getEnablePageAnnotation(HandlerMethod handlerMethod) {

        EnablePage enablePage = handlerMethod.getMethodAnnotation(EnablePage.class);
        if (enablePage == null) {

            enablePage = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), EnablePage.class);
        }
        return enablePage;
    }

    @Nullable
    static EnablePage getEnablePageAnnotation(MethodParameter parameter) {

        EnablePage enablePage = parameter.getMethodAnnotation(EnablePage.class);
        if (enablePage == null) {

            enablePage = AnnotationUtils.findAnnotation(parameter.getContainingClass(), EnablePage.class);
        }
        return enablePage;
    }

    static boolean supportsMethodArgumentResolver(MethodParameter parameter) {

        return hasEnablePageAnnotation(parameter)
                && (parameter.hasParameterAnnotation(PageNumber.class) || parameter.hasParameterAnnotation(PageSize.class));

    }

    static boolean supportsResponseBodyAdvice(MethodParameter returnType) {

        EnablePage enablePage = getEnablePageAnnotation(returnType);
        return enablePage != null && enablePage.enable() && enablePage.paddings().length > 0;
    }

    public static String getPageNumberParam() {

        return getPageInfoIfExist(PageInfo::getPageNumberParam);
    }

    public static String getPageSizeParam() {

        return getPageInfoIfExist(PageInfo::getPageSizeParam);
    }

    public static Integer getExpectedPageNum() {

        return getPageInfoIfExist(PageInfo::getExpectedPageNum);
    }

    public static Integer getExpectedPageSize() {

        return getPageInfoIfExist(PageInfo::getExpectedPageSize);
    }

    public static Integer getActualPageNum() {

        return getPageInfoIfExist(PageInfo::getActualPageNum);
    }

    public static Integer getActualPageSize() {

        return getPageInfoIfExist(PageInfo::getActualPageSize);
    }

    public static Long getActualTotal() {

        return getPageInfoIfExist(PageInfo::getActualTotal);
    }

    static PageInfo getCurrentPageInfo() {

        return (PageInfo) RequestContextHolder.currentRequestAttributes().getAttribute(PAGE_INFO, RequestAttributes.SCOPE_REQUEST);
    }

    static void setCurrentPageInfo(PageInfo pageInfo) {

        RequestContextHolder.currentRequestAttributes().setAttribute(PAGE_INFO, pageInfo, RequestAttributes.SCOPE_REQUEST);
    }

    static void updatePageInfo() {

        PageInfo currentPageInfo = getCurrentPageInfo();
        Assert.state(currentPageInfo != null, "Paging is not enabled for the current request");
        currentPageInfo.updateActualPageResult();
    }

    private static <T> T getPageInfoIfExist(Function<PageInfo, T> function) {

        PageInfo pageInfo = getCurrentPageInfo();
        return pageInfo == null ? null : function.apply(pageInfo);
    }
}
