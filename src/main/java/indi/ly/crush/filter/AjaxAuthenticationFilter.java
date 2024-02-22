package indi.ly.crush.filter;

import indi.ly.crush.response.ResponseResult;
import indi.ly.crush.response.ResponseResultEnum;
import indi.ly.crush.util.support.BaseJacksonUtil;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <h2>认证过滤器</h2>
 * <p>
 *     用于处理通过 {@code Ajax} 发起的未认证请求. <br />
 *     当访问受保护资源但用户未登录时, 返回 {@code HTTP} 状态码 {@code 401} 而不是重定向到登录页面.
 * </p>
 * <br />
 *
 * <h2>使用背景</h2>
 * <p>
 *     当前端采用 {@code Vue} 等单页应用(<em>SPA</em>)框架时, 页面的路由处理通常在客户端进行, 而不是通过服务器端的重定向. <br />
 *     因此, {@code Shiro} 的 {@link ShiroFilterFactoryBean#setLoginUrl(String) setLoginUrl}、
 *     {@link ShiroFilterFactoryBean#setSuccessUrl(String) setSuccessUrl}、
 *     {@link ShiroFilterFactoryBean#setUnauthorizedUrl(String) setUnauthorizedUrl} 等用于配置页面重定向的方法需要以不同的方式来处理. <br /> <br />
 *
 *     这些设置在 {@code SPA} 架构下不再适用, 因为它们假设服务器能够直接控制页面跳转, 而在 {@code SPA} 应用中, 页面跳转通常是通过前端路由来控制的.
 * </p>
 *
 * @since 1.0
 * @see FormAuthenticationFilter#onAccessDenied(ServletRequest, ServletResponse)
 * @see AccessControlFilter#saveRequestAndRedirectToLogin(ServletRequest, ServletResponse)
 * @see AccessControlFilter#redirectToLogin(ServletRequest, ServletResponse)
 * @see AccessControlFilter#getLoginUrl()
 * @see AccessControlFilter#DEFAULT_LOGIN_URL
 * @author 云上的云
 * @formatter:off
 */
public class AjaxAuthenticationFilter
        extends FormAuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ResponseResult<?> result = ResponseResult.set(ResponseResultEnum.UNAUTHORIZED);
        String msg = BaseJacksonUtil.wrap(om -> om.writeValueAsString(result));
        httpResponse.getWriter().write(msg);

        return false; // 阻止 Shiro 继续处理该请求, 这意味着请求将在此过滤器中终止.
    }
}
