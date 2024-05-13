package indi.ly.crush.realm;

import indi.ly.crush.model.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.springframework.lang.Nullable;

/**
 * <h2>用户信息校验器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
final class UserInfoValidator {
    /**
     * <p>
     *     验证给定的用户信息是否有效.
     * </p>
     *
     * @param user 要验证的用户信息.
     */
    public static void validate(@Nullable User user) {
        if (user == null) {
            throw new AuthenticationException("用户不存在.");
        }

        if (user.getLocked()) {
            throw new LockedAccountException("账号已被锁定.");
        }

        // 确保密码和盐得到妥善处理.
        if (user.getPassword() == null || user.getSalt() == null) {
            throw new AuthenticationException("用户认证信息不完整.");
        }
    }
}
