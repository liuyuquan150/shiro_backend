package indi.ly.crush.constants;

import indi.ly.crush.filter.AnyOfRolesAuthorizationFilter;
import org.apache.shiro.web.filter.authc.*;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.filter.authz.PortFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.filter.session.NoSessionCreationFilter;

/**
 * <h2>{@code Shiro} 安全策略关键字</h2>
 *
 * <h2>过滤器之间的必要关系</h2>
 * <ol>
 *     <li>
 *         优先级和顺序: <br />
 *         过滤器在配置时的顺序决定了它们的执行顺序(<em>{@code URL} 模式定义遵循 "先匹配者胜 " 的原则</em>). <br />
 *         通常, 更具体的规则应该放在前面, 而更通用的规则放在后面. <br />
 *         例如, 对于特定 {@code URL} 路径的访问控制(<em>如角色或权限限制</em>)应该先于对所有 {@code URL} 的通用访问控制(<em>如需要认证</em>)进行配置. <br /> <br />
 *
 *         关于这一点, {@link AnonymousFilter} 源码的类注释上也阐述了这一点并举了例子.
 *     </li>
 *     <li>
 *         依赖关系: <br />
 *         某些过滤器的作用依赖于前面过滤器的处理结果. <br />
 *         例如, {@code roles} 和 {@code perms} 过滤器依赖于用户的认证信息, 这意味着它们通常在 {@code authc} 或 {@code user} 过滤器之后配置.
 *     </li>
 *     <li>
 *         互斥和兼容性: <br />
 *         有些过滤器不能同时应用于同一路径, 因为它们的作用是互斥的. <br />
 *         例如, {@code anon} 和 {@code authc} 是互斥的, 一个路径不能同时要求匿名访问和用户认证.
 *     </li>
 * </ol>
 *
 * <h2>使用过滤器的注意事项</h2>
 * <ol>
 *     <li>
 *         安全策略设计: <br />
 *         在配置过滤器之前, 需要有一个清晰的安全策略设计. <br />
 *         确定哪些资源是公开的, 哪些需要认证, 哪些需要额外的权限或角色才能访问.
 *     </li>
 *     <li>
 *         默认拒绝原则: <br />
 *         遵循最小权限原则和默认拒绝原则, 即除非明确允许, 否则默认拒绝所有访问. <br />
 *         这意味着应该将更通用的认证和授权过滤器(如 {@code authc})应用于大部分资源, 而只为特定资源配置更宽松的访问规则(如 {@code anon}).
 *     </li>
 *     <li>
 *         过滤器配置的细粒度: <br />
 *         应该尽可能细化过滤器的应用范围, 而不是将所有操作都要求具有相同的权限或角色,
 *         应该根据实际的访问控制需求为不同的操作指定不同的权限或角色.
 *     </li>
 *     <li>
 *         会话管理: <br />
 *         在使用会话相关的过滤器(如 {@code noSessionCreation})时, 要注意应用的会话管理策略, 确保它不会意外地影响用户的登录状态或权限.
 *     </li>
 * </ol>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public final class ShiroSecurityPolicyKeywordConstants {
    /**
     * <p>
     *     {@link AnonymousFilter 匿名过滤器}
     *     <ul>
     *         <li>
     *             含义: 允许匿名访问, 即不需要进行登录或者身份认证就可以访问指定的路径或资源.
     *         </li>
     *         <li>
     *             用途: 通常用于静态资源(如 CSS、JavaScript 文件)或者某些公开页面(登录页面、首页)的访问控制.
     *         </li>
     *     </ul>
     * </p>
     *
     * @see AnonymousFilter
     */
    public static final String ANON = "anon";
    /**
     * <p>
     *     {@link AuthenticationFilter 认证过滤器}
     * </p>
     *
     * @see AuthenticationFilter
     */
    public static final String AUTHC = "authc";
    /**
     * <p>
     *     {@link BasicHttpAuthenticationFilter 基础 Http 认证过滤器}
     * </p>
     *
     * @see BasicHttpAuthenticationFilter
     */
    public static final String AUTHC_BASIC = "authcBasic";
    /**
     * <p>
     *     {@link LogoutFilter 登出过滤器}
     * </p>
     *
     * @see LogoutFilter
     */
    public static final String LOGOUT = "logout";
    /**
     * <p>
     *     {@link NoSessionCreationFilter 无会话创建过滤器}
     * </p>
     *
     * @see NoSessionCreationFilter
     */
    public static final String NO_SESSION_CREATION = "noSessionCreation";
    /**
     * <p>
     *     {@link PermissionsAuthorizationFilter 权限授权过滤器}
     * </p>
     *
     * @see PermissionsAuthorizationFilter
     */
    public static final String PERMS = "perms";
    /**
     * <p>
     *     {@link PortFilter 端口过滤器}
     * </p>
     *
     * @see PortFilter
     */
    public static final String PORT = "port";
    /**
     * <p>
     *     {@link HttpMethodPermissionFilter 方法权限过滤器}
     * </p>
     *
     * @see HttpMethodPermissionFilter
     */
    public static final String REST = "rest";
    /**
     * <p>
     *     {@link RolesAuthorizationFilter 角色授权过滤器}
     * </p>
     *
     * @see RolesAuthorizationFilter
     */
    public static final String ROLES = "roles";
    /**
     * <p>
     *     {@link SslFilter SSL 过滤器}
     * </p>
     *
     * @see SslFilter
     */
    public static final String SSL = "ssl";
    /**
     * <p>
     *     {@link UserFilter 用户过滤器}
     * </p>
     *
     * @see UserFilter
     */
    public static final String USER = "user";


    /*--------------------------------------------
    |                  自定义过滤器                 |
    ============================================*/


    /**
     * <p>
     *     {@link AnyOfRolesAuthorizationFilter 任意角色授权过滤器}
     * </p>
     *
     * @see AnyOfRolesAuthorizationFilter
     */
    public static final String ANY_OF_ROLES = "anyOfRoles";

    private ShiroSecurityPolicyKeywordConstants() {}
}
