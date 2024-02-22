package indi.ly.crush.provider;

import lombok.NonNull;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * <h2>基于 {@code Shiro} 的用户名提供者</h2>
 * <p>
 *     利用 {@code Apache Shiro} 提供的安全上下文, 为插入(<em>持久化</em>)和更新实体时提供用户名.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class ShiroBasedUsernameProvider
        implements UsernameProvider {
    @Override
    public @NonNull String getCurrentUsername() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            // getPrincipal(): 当前用户的主要信息. 通常, 这个主要信息是一个用户名字符串, 但实际值取决于你的应用如何在 Shiro 中配置身份验证.
            Object principal = subject.getPrincipal();
            if (principal != null) {
                return principal.toString();
            }
        }
        // 返回 "anonymous" 或其它默认值对于未认证的用户是一个常见做法.
        return "anonymous";
    }
}
