package indi.ly.crush.filter;

import indi.ly.crush.response.ResponseResult;
import indi.ly.crush.response.ResponseResultEnum;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * <h2>任意角色授权过滤器</h2>
 * <p>
 *     {@code OR} 逻辑的角色过滤({@link RolesAuthorizationFilter} 是 {@code AND} 逻辑的角色过滤), 即用户拥有列出的任意角色就可以访问资源.
 * </p>
 * <br />
 *
 * <h2>使用场景</h2>
 * <p>
 *     需要对访问权限进行更灵活管理时, 特别是当你希望用户具有多个潜在角色中的任何一个就能访问某个资源或执行某个操作时. <br />
 *     这种 {@code OR} 逻辑的角色过滤允许更宽松的权限控制, 适用于多种业务场景.
 * </p>
 * <br />
 *
 * <h2>示例场景</h2>
 * <p>
 *     假设你正在开发一个内容管理系统({@code CMS}), 其中包含多种类型的内容, 比如新闻、博客和教程. <br />
 *     对于某些特定的操作或内容区域, 你可能希望允许不同类型的用户访问:
 *     <ul>
 *         <li>新闻编辑: 负责创建和编辑新闻内容.</li>
 *         <li>博客作者：负责撰写和管理博客帖子.</li>
 *         <li>教程贡献者：专注于创建和更新教程.</li>
 *     </ul>
 *     虽然这些角色各自有其特定的职责, 但在某些情况下, 你可能希望允许任何一种类型的内容创作者访问某些共享资源. <br />
 *     例如, 可能有一个资源库, 里面存放可以被新闻、博客或教程中使用的共享图像和视频. <br /> <br />
 *
 *     在这种情况下, 你可以使用本过滤器来允许任何一种类型的内容创作者访问这个资源库:
 *     <pre>{@code
 *     				@Bean
 *     				public ShiroFilterFactoryBean createShiroFilterFactoryBeanBean(
 *     					DefaultWebSecurityManager defaultWebSecurityManager
 *     				) {
 *     					ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
 *     					shiroFilter.setSecurityManager(defaultWebSecurityManager);
 *     					Map<String, Filter> filters = new LinkedHashMap<>();
 *     					filters.put("anyOfRoles", new AnyOfRolesAuthorizationFilter());
 *     					shiroFilter.setFilters(filters);
 *
 *     					Map<String, String> filterChain = new HashMap<>();
 *     					filterChain.put("/resource-library/**",
 *     						"anyOfRoles[newsEditor, blogAuthor, tutorialContributor]");
 *     					shiroFilter.setFilterChainDefinitionMap(filterChain);
 *     					return shiroFilter;
 *     				}
 *     	}</pre>
 * </p>
 *
 * @since 1.0
 * @see RolesAuthorizationFilter#isAccessAllowed(ServletRequest, ServletResponse, Object)
 * @see RequiresPermissions
 * @see RequiresPermissions#logical()
 * @see Logical#OR
 * @author 云上的云
 * @formatter:off
 */
public class AnyOfRolesAuthorizationFilter
		extends AuthorizationFilter {
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		Subject subject = super.getSubject(request, response);
		String[] rolesArray = (String[]) mappedValue;

		if (rolesArray == null || rolesArray.length == 0) {
			// 默认没有角色限制, 允许访问. @see RolesAuthorizationFilter.isAccessAllowed
			return true;
		}

		// 遍历角色. 如果用户具有列表中的任一角色, 则允许访问; 如果用户不具有任何角色, 则拒绝访问.
		return Arrays.stream(rolesArray).anyMatch(subject :: hasRole);
	}

	@Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
		    throws IOException {
		JsonResponseBuilder.buildJsonResponse(
				WebUtils.toHttp(response),
				HttpServletResponse.SC_FORBIDDEN,
				ResponseResult.set(ResponseResultEnum.FORBIDDEN)
		);
        return false; // 阻止 Shiro 继续处理该请求, 这意味着请求将在此过滤器中终止.
    }
}
