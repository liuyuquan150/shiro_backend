package indi.ly.crush.config;

import indi.ly.crush.filter.AnyOfRolesAuthorizationFilter;
import indi.ly.crush.filter.CustomizableResponseFormAuthenticationFilter;
import indi.ly.crush.filter.CustomizableResponseRolesAuthorizationFilter;
import indi.ly.crush.realm.UserRealm;
import indi.ly.crush.repository.IUserRepository;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.tomcat.util.net.openssl.ciphers.MessageDigest;
import org.springframework.beans.factory.annotation.Value;
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
 * @see <a href="https://developer.aliyun.com/article/849911">Shiro实现记住我(十)</a>
 * @author 云上的云
 * @formatter:off
 */
@Configuration
public class ShiroConfig {

    @Value("${app.cipher-key}")
    private String cipherKeyBase64;

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
    public CookieRememberMeManager createCookieRememberMeManagerBean(AppProperties appProperties) {
        AppProperties.RememberMeConfig rememberMeConfig = appProperties.getRememberMe();

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();

        SimpleCookie rememberMeCookie = new SimpleCookie(rememberMeConfig.getCookieName());
        rememberMeCookie.setMaxAge(rememberMeConfig.getMaxAge());
        cookieRememberMeManager.setCookie(rememberMeCookie);

        /*
            使用强大的加密算法和密钥来保护 “记住我” cookie.
            确保使用安全的算法(如 AES)和足够长度的密钥（如 256 位), 并且密钥应该是随机生成的.
            密钥应该安全存储, 避免硬编码在配置文件或代码中. 考虑使用环境变量或安全的配置管理服务来存储密钥.
         */
        cookieRememberMeManager.setCipherKey(Base64.decode(this.cipherKeyBase64));
        return cookieRememberMeManager;
    }

    @Bean
    public DefaultWebSecurityManager createDefaultWebSecurityManagerBean(
            List<Realm> realms, RememberMeManager rememberMeManager
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置此 SecurityManager 实例管理的 Realm(SecurityManager 的认证器、授权器要完成校验, 需要 Realm 提供安全信息).
        securityManager.setRealms(realms);
        // 配置记住我管理器.
        securityManager.setRememberMeManager(rememberMeManager);
        // 配置缓存管理.
//        securityManager.setCacheManager();
        return securityManager;
    }

    @Bean(name = "shiroFilterFactoryBean")
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
     *         <li>{@code roles}: 使用自定义响应角色授权过滤器.</li>
     *         <li>{@code anyOfRoles}: 使用自定义的任意角色授权过滤器, 允许基于多个角色进行授权.</li>
     *         <li>{@code authc}: 使用自定义响应表单认证过滤器.</li>
     *     </ul>
     * </p>
     *
     * @return 一个包含自定义过滤器配置的 {@link Map} 对象.
     */
    private Map<String, Filter> createFilters() {
        Map<String, Filter> filters = new LinkedHashMap<>(3);
        filters.put(ROLES, new CustomizableResponseRolesAuthorizationFilter());
        filters.put(ANY_OF_ROLES, new AnyOfRolesAuthorizationFilter());
        filters.put(AUTHC, new CustomizableResponseFormAuthenticationFilter());
        return filters;
    }

    /* 过滤器链定义(Filter Chain Definitions)是用来指定哪些资源是受保护的以及它们受到哪种类型的安全策略保护. */

    /**
     * <p>
     *     定义 {@code Shiro} 过滤器链. <br /> <br />
     *
     *     此方法配置了应用中不同 {@code API} 路径的安全策略, 具体路径和策略包括:
     *     <ul>
     *         <li>/api/v1/register: 允许匿名访问的用户注册 {@code API}.</li>
     *         <li>/api/v1/login: 允许匿名访问的用户登录 {@code API}.</li>
     *         <li>/api/v1/guest/**: 允许匿名访问的游客路径 {@code API} 路径.</li>
     *         <li>/api/v1/logout: 执行用户登出逻辑的 {@code API}.</li>
     *         <li>/api/v1/**: 默认情况下, 所有其它 {@code API} 路径都需要已认证后才能访问.</li>
     *     </ul>
     *
     *     在 {@code Apache Shiro} 框架中, 过滤器链定义({@code Filter Chain Definitions})是一个非常重要的概念, 用于指定如何对应用中的不同URL路径应用不同的安全策略. <br />
     *     这些安全策略包括认证({@code authc})、授权({@code roles}、{@code perms} 等)、匿名访问({@code anon})等. <br />
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
     *             一个常见的做法是在过滤器链定义的最后放置一个捕获所有请求的规则(如 /**、/api/**), 以应用默认的安全策略. <br />
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
     *         <li>对于路径 /api/special/**, 请求首先需要认证(authc), 然后需要拥有 special 角色(roles[special]).</li>
     *         <li>对于路径 /api/**, 请求允许匿名访问.</li>
     *         <li>所有其它请求(/**)需要认证.</li>
     *     </ul>
     * </p>
     *
     * @return 一个包含路径模式与安全策略映射的 {@link Map} 对象.
     */
    private Map<String, String> createFilterChainDefinitionMap() {
        Map<String, String> filterChain = new LinkedHashMap<>(5);
        filterChain.put("/api/v1/register", ANON);
        filterChain.put("/api/v1/login", ANON);
        filterChain.put("/api/v1/guest/**", ANON);
        filterChain.put("/api/v1/**", AUTHC); // 配置所有 /api/** 路径下的请求都需要通过自定义的 authc 过滤器(即 AuthenticationFilter). 其它API需要认证(登录)后访问.
        return filterChain;
    }
}
