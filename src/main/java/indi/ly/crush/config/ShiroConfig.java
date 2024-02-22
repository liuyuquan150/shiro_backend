package indi.ly.crush.config;

import indi.ly.crush.filter.AjaxAuthenticationFilter;
import indi.ly.crush.realm.UserRealm;
import indi.ly.crush.repository.IUserRepository;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static indi.ly.crush.constants.ShiroSecurityPolicyKeywordConstants.AUTHC;

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
        h.setHashAlgorithmName("md5");
        // 指定哈希算法的散列(循环)次数.
        h.setHashIterations(3);
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

        /* 自定义过滤器设置 */
        Map<String, javax.servlet.Filter> filters = new LinkedHashMap<>();
        // 使用自定义的 AuthenticationFilter 替换默认的 authc 过滤器.
        filters.put(AUTHC, new AjaxAuthenticationFilter());
        shiroFilter.setFilters(filters);

        /* 过滤器链定义(Filter Chain Definitions)是用来指定哪些资源是受保护的以及它们受到哪种类型的安全策略保护. */
        Map<String, String> filterChain = new LinkedHashMap<>(5);
        filterChain.put("/api/v*/login", "anon"); // 登录 API.
        filterChain.put("/api/v*/logout", "logout"); // 登出 API
        filterChain.put("/api/v*/guest/**", "anon");  // 游客.
        filterChain.put("/api/v*/**", AUTHC); // 配置所有 /api/** 路径下的请求都需要通过自定义的 authc 过滤器(即 AuthenticationFilter). 其它API需要认证(登录)后访问.
        shiroFilter.setFilterChainDefinitionMap(filterChain);

        return shiroFilter;
    }
}
