package indi.ly.crush.filter;

import indi.ly.crush.response.ResponseResult;
import indi.ly.crush.response.ResponseResultEnum;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h2>自定义响应角色授权过滤器</h2>
 * <p>
 *     一个自定义的响应角色授权过滤器, 用于替代标准的{@link RolesAuthorizationFilter 角色授权过滤器}. <br />
 *     它的主要作用是在访问被拒绝时(请浏览 {@link #isAccessAllowed} 方法), 用自定义的 {@code JSON} 响应代替默认的 {@code JSON} 响应.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class CustomizableResponseRolesAuthorizationFilter
        extends RolesAuthorizationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        JsonResponseBuilder.buildJsonResponse(
                WebUtils.toHttp(response),
                HttpServletResponse.SC_FORBIDDEN,
                ResponseResult.set(ResponseResultEnum.FORBIDDEN)
        );
        return false; // 阻止 Shiro 继续处理该请求, 这意味着请求将在此过滤器中终止.
    }
}
