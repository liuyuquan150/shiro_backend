package indi.ly.crush.realm;

import indi.ly.crush.model.entity.User;
import indi.ly.crush.repository.IUserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;
import java.util.Set;

/**
 * <h2>用户安全域(<em>用户认证授权域</em>)</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class UserRealm
        extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);
    private final IUserRepository userRepository;

    public UserRealm(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String) principals.getPrimaryPrincipal();
        LOGGER.debug("为用户 [{}] 加载角色和权限.", username);

        Set<String> roles = this.userRepository.findRolesByUsername(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);

        LOGGER.info("用户 [{}] 加载的角色 [{}].", username, roles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();

        LOGGER.debug("正在认证用户 [{}].", username);

        if (username == null) {
            LOGGER.error("尝试使用空用户名进行认证.");
            throw new UnknownAccountException("用户名不存在.");
        }

        Example<User> userExample = Optional
                                            .of(username)
                                            .map(User :: new)
                                            .map(Example :: of)
                                            .get();


        Optional<User> userOptional = this.userRepository.findOne(userExample);
        if (userOptional.isEmpty()) {
            LOGGER.error("未找到用户 [{}].", username);
            throw new AuthenticationException("用户不存在.");
        }

        User user = userOptional.get();
        // 确保密码和盐得到妥善处理.
        if (user.getPassword() == null || user.getSalt() == null) {
            throw new AuthenticationException("用户认证信息不完整.");
        }

        LOGGER.info("用户 [{}] 认证成功.", username);

        // Shiro 会自动验证密码是否匹配.
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(), // 数据库中的加密密码.
                ByteSource.Util.bytes(user.getSalt()),
                getName() // 当前 Realm 的名称.
        );
    }
}