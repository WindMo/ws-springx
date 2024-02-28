package ws.mybatis.spring.page;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ws.mybatis.spring.annotation.EnablePage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于拦截器实现{@link EnablePage}分页功能
 *
 * @author WindShadow
 * @version 2021-12-28.
 */

class PageHandlerInterceptor implements HandlerInterceptor {

    /**
     * @param request  the request
     * @param response the response
     * @param handler  {@link HandlerMethod}
     * @return always true
     * @throws Exception in case of errors
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        EnablePage enablePage = null;
        if (handler instanceof HandlerMethod) {

            enablePage = PageInfoHolder.getEnablePageAnnotation((HandlerMethod) handler);
        } else {

            // TODO
        }
        if (enablePage != null) {
            PageInfo pageInfo = doStartPageIfNecessary(enablePage, request);
            PageInfoHolder.setCurrentPageInfo(pageInfo);
        }
        return true;
    }

    /**
     * 执行分页，如果必要的话
     *
     * @param enablePage 分页配置注解
     * @param request    请求对象
     * @return 分页信息
     */
    private PageInfo doStartPageIfNecessary(@NonNull EnablePage enablePage, @NonNull HttpServletRequest request) {

        Assert.notNull(enablePage, "enablePage must not be null");
        Assert.notNull(request, "request must not be null");

        String pageNumberParam = enablePage.pageNumberParam();
        String pageSizerParam = enablePage.pageSizeParam();
        Page<?> page = null;

        WebDataBinder binder = new WebDataBinder(null);
        Integer expectedPageNum = binder.convertIfNecessary(request.getParameter(pageNumberParam), Integer.class);
        Integer expectedPageSize = binder.convertIfNecessary(request.getParameter(pageSizerParam), Integer.class);

        expectedPageNum = expectedPageNum == null ? enablePage.defaultPageNumber() : expectedPageNum;
        expectedPageSize = expectedPageSize == null ? enablePage.defaultPageSize() : expectedPageSize;

        if (enablePage.enable()) {
            page = PageHelper.startPage(expectedPageNum, expectedPageSize);
        }

        return new PageInfo(pageNumberParam, pageSizerParam, expectedPageNum, expectedPageSize, null, null, null, page);
    }
}
