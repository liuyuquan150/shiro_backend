package indi.ly.crush.provider;

import indi.ly.crush.config.ShiroConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

/**
 * <h2>基于 {@code Shiro} 的用户名提供者</h2>
 * <p>
 *     利用 {@code Apache Shiro} 提供的安全上下文, 为插入(<em>持久化</em>)和更新实体时提供用户名.
 * </p>
 * <br />
 *
 * <h2>关于 {@code SecurityUtils.setSecurityManager(securityManager)} 步骤的省略</h2>
 * <p>
 *     在 {@code Shiro} 与 {@code Spring Boot} 整合的应用中, 通常通过 {@code Spring} 的配置类({@link ShiroConfig}) 来配置 {@link SecurityManager},
 *     如使用 {@link Bean @Bean} 注解定义一个 {@link SecurityManager} 的 {@link ShiroConfig#createDefaultWebSecurityManagerBean Bean}. <br /> <br />
 *
 *     这样, {@code Spring Boot} 的自动配置机制会负责创建和初始化 {@link SecurityManager}, 并将其注入到应用中需要的地方. <br />
 *     这个过程无需手动调用 {@link SecurityUtils#setSecurityManager(SecurityManager)} 方法,
 *     因为 {@code Spring} 的依赖注入机制会自动处理(<em>关于 {@link SubjectThreadState#bind()} 方法的执行时机</em>). <br /> <br />
 *
 *     只有绑定了{@link SecurityManager 安全管理器}, 之后才可以通过 {@link SecurityUtils#getSubject()} 方法创建 {@link Subject} 实例.
 * </p>
 *
 * @since 1.0
 * @see SubjectThreadState#bind()
 * @see Subject.Builder#Builder()
 * @see SecurityUtils#getSecurityManager()
 * @author 云上的云
 * @formatter:off
 */
public class ShiroBasedUsernameProvider
        implements UsernameProvider {
    @Override
    public @NonNull String getCurrentUsername() {
        Subject subject = null;

        try {
            subject = SecurityUtils.getSubject();
        } catch (UnavailableSecurityManagerException ignored) {
            /*
                如果你在一个由 Shiro 管理之外的线程中调用 SecurityUtils.getSubject(),
                例如在一个没有经过 Shiro 过滤器处理的后台线程中, 可能会抛出异常, 因为 Shiro 的 Subject 和 SecurityManager 的绑定是基于当前线程的(使用 ThreadLocal 存储).
                在这种情况下, Shiro 可能无法正确地为当前线程提供 Subject 实例或 SecurityManager 实例.

                一个没有经过 Shiro 过滤器处理的后台线程: 例如在 ApplicationRunner 中通过 JPA 实体操作数据表, 此 JPA 实体又被 JPA 监听器所监听, 因此调用了本方法.

                @see Subject.Builder#Builder()、@see SecurityUtils#getSecurityManager()
             */
        }

        if (subject != null) {
            // getPrincipal(): 当前用户的主要信息. 通常, 这个主要信息是一个用户名字符串, 但实际值取决于你的应用如何在 Shiro 中配置身份验证.
            Object principal = subject.getPrincipal();
            if (principal != null) {
                return principal.toString();
            }
        }
        // 返回 "anonymous" 或其它默认值对于未认证的用户是一个常见做法.
        return "SYSTEM";
    }
}
