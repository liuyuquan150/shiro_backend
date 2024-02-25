package indi.ly.crush.config;

import indi.ly.crush.filter.AnyOfRolesAuthorizationFilter;
import indi.ly.crush.filter.CustomizableResponseFormAuthenticationFilter;
import indi.ly.crush.filter.CustomizableResponseRolesAuthorizationFilter;
import indi.ly.crush.realm.UserRealm;
import indi.ly.crush.repository.IUserRepository;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.tomcat.util.net.openssl.ciphers.MessageDigest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static indi.ly.crush.constants.ShiroSecurityPolicyKeywordConstants.*;

/**
 * <h2>Shiro 配置</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Configuration
public class ShiroConfig {

    @Bean
    public HashedCredentialsMatcher createHashedCredentialsMatcherBean() {
        HashedCredentialsMatcher h = new HashedCredentialsMatcher();
        // 指定加密方法为 md5.
        h.setHashAlgorithmName(MessageDigest.MD5.name());
        // 指定哈希算法的散列(循环)次数.
        h.setHashIterations(1024);
        return h;
    }

    @Bean
    public UserRealm createUserRealmBean(HashedCredentialsMatcher matcher, IUserRepository userRepository) {
        UserRealm userRealm = new UserRealm(userRepository);
        // 设置身份验证尝试中使用的凭证匹配器, 以验证提交的凭证(Token 中的密码, 先对其进行加密处理)与系统中存储的凭证(数据库 中的已加密密码)是否一致.
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    @Bean
    public DefaultWebSecurityManager createDefaultWebSecurityManagerBean(List<Realm> realms) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置此 SecurityManager 实例管理的 Realm(SecurityManager 的认证器、授权器要完成校验, 需要 Realm 提供安全信息).
        securityManager.setRealms(realms);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean createShiroFilterFactoryBeanBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(defaultWebSecurityManager);
        shiroFilter.setFilters(this.createFilters());
        shiroFilter.setFilterChainDefinitionMap(this.createFilterChainDefinitionMap());
        return shiroFilter;
    }

    /**
     * <p>
     *     创建并配置自定义的 {@code Shiro} 过滤器. <br /> <br />
     *
     *     该方法定义了几种自定义过滤器, 用于不同的安全需求, 包括:
     *     <ul>
     *         <li>authc: 使用自定义响应表单认证过滤器.</li>
     *         <li>roles: 使用自定义响应角色授权过滤器.</li>
     *         <li>authc: 使用自定义的任意角色授权过滤器, 允许基于多个角色进行授权.</li>
     *     </ul>
     * </p>
     *
     * @return 一个包含自定义过滤器配置的 {@link Map} 对象.
     */
    private Map<String, Filter> createFilters() {
        Map<String, Filter> filters = new LinkedHashMap<>(3);
        filters.put(AUTHC, new CustomizableResponseFormAuthenticationFilter());
        filters.put(ROLES, new CustomizableResponseRolesAuthorizationFilter());
        filters.put(ANY_OF_ROLES, new AnyOfRolesAuthorizationFilter());
        return filters;
    }

    /* 过滤器链定义(Filter Chain Definitions)是用来指定哪些资源是受保护的以及它们受到哪种类型的安全策略保护. */

    /**
     * <p>
     *     定义 {@code Shiro} 过滤器链. <br /> <br />
     *
     *     此方法配置了应用中不同 {@code API} 路径的安全策略, 具体路径和策略包括:
     *     <ul>
     *         <li>{@code "/api/v1/users"}: 允许匿名访问的注册 {@code API}.</li>
     *         <li>{@code "/api/v1/login"}: 允许匿名访问的登录 {@code API}.</li>
     *         <li>{@code "/api/v1/logout"}: 应用登出逻辑.</li>
     *         <li>{@code "/api/v1/guest/**"}: 允许匿名访问的游客 {@code API} 路径.</li>
     *         <li>{@code "/api/v1/**"}: 所有其它 {@code API} 路径都需要认证后访问.</li>
     *     </ul>
     *
     *     在 {@code Apache Shiro} 框架中, 过滤器链定义(Filter Chain Definitions)是一个非常重要的概念, 用于指定如何对应用中的不同URL路径应用不同的安全策略. <br />
     *     这些安全策略包括认证(authc)、授权(roles、perms 等)、匿名访问(anon)等. <br />
     *     过滤器链定义通过一系列的键值对来配置, 其中键是指 {@code URL} 模式(支持 {@code Ant} 风格的路径模式), 值是对应的过滤器链. <br /> <br />
     *
     *     过滤器链的顺序要求 <br />
     *     {@code Shiro} 的过滤器链定义是有顺序要求的, 这是因为 {@code Shiro} 将根据添加过滤器到链中的顺序来应用这些过滤器. <br />
     *     {@code Shiro} 会按照定义的顺序, 对匹配的请求依次应用每个过滤器, 直到某个过滤器处理了请求(比如进行了认证跳转)或者请求通过了所有过滤器. <br />
     *     这意味着:
     *     <ol>
     *         <li>
     *             顺序很重要: <br />
     *             如果你有多个规则可以匹配同一个 {@code URL} 路径, 那么 {@code Shiro} 只会应用第一个匹配的规则. <br />
     *             因此, 你需要仔细考虑过滤器链的定义顺序, 确保它们按照你期望的方式工作.
     *         </li>
     *         <li>
     *             更具体的规则应该放在前面: <br />
     *             通常, 更具体的 {@code URL} 路径匹配规则应该放在更通用的规则之前. <br />
     *             比如, 对于特定的 {@code API} 端点应用特定的安全策略, 应该先定义这些特定端点的规则, 然后再定义更通用的规则.
     *         </li>
     *         <li>
     *             默认规则应放在最后: <br />
     *             一个常见的做法是在过滤器链定义的最后放置一个捕获所有请求的规则(如 {@code /**}、{@code /api/**}), 以应用默认的安全策略. <br />
     *             这确保了所有未被前面规则匹配的请求都会被处理.
     *         </li>
     *     </ol>
     *
     *     假设我们有以下的过滤器链定义:
     *     <pre>{@code
     *                  Map<String, String> filterChain = new LinkedHashMap<>();
     *                  filterChain.put("/api/special/**", "authc, roles[special]");
     *                  filterChain.put("/api/**", "anon");
     *                  filterChain.put("/**", "authc");
     *     }</pre>
     *     <ul>
     *         <li>对于路径 {@code /api/special/**}, 请求首先需要认证({@code authc}), 然后需要拥有 {@code special} 角色({@code roles[special]}).</li>
     *         <li>对于路径 {@code /api/**}, 请求允许匿名访问.</li>
     *         <li>所有其它请求({@code /**})需要认证.</li>
     *     </ul>
     * </p>
     *
     * @return 一个包含路径模式与安全策略映射的 {@link Map} 对象.
     */
    private Map<String, String> createFilterChainDefinitionMap() {
        Map<String, String> filterChain = new LinkedHashMap<>(5);
        filterChain.put("/api/v1/users", ANON);
        filterChain.put("/api/v1/login", ANON);
        filterChain.put("/api/v1/logout", LOGOUT);
        filterChain.put("/api/v1/guest/**", ANON);
        filterChain.put("/api/v1/**", AUTHC); // 配置所有 /api/** 路径下的请求都需要通过自定义的 authc 过滤器(即 AuthenticationFilter). 其它API需要认证(登录)后访问.
        return filterChain;
    }
}
