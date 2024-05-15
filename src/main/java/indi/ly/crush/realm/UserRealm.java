package indi.ly.crush.realm;

import indi.ly.crush.json.jaskson.desensitize.DesensitizeStrategyEnum;
import indi.ly.crush.model.entity.User;
import indi.ly.crush.repository.IUserRepository;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * <h2>用户安全域(用户认证授权域)</h2>
 *
 * <h2>授权须知</h2>
 * <p>
 *     一个用户可能不具备某个指定的角色, 但它是有可能拥有这个指定角色的权限的. <br />
 *     这是一种设计, 这种设计提供了更高的灵活性和细粒度的控制, 允许系统管理员对用户的权限进行精细调整. <br /> <br />
 *
 *     举个例子:
 *     <ul>
 *         <li>
 *             假设{@code 编辑文章}的权限通常只赋予{@code 编辑角色}.
 *         </li>
 *         <li>
 *             但是, 可能有一个特例用户(不是{@code 编辑角色}的成员), 因为某些原因, 他需要获得{@code 编辑文章的权限}.
 *         </li>
 *         <li>
 *             在这种情况下, 系统管理员可以直接给这个用户赋予{@code 编辑文章的权限}, 而不需要将他归入{@code 编辑角色}.
 *         </li>
 *     </ul>
 *     这种方法的优点是提供了极大的灵活性, 使得权限管理可以非常精确地控制每个用户能做什么, 而不是仅仅依赖角色这一粗糙的划分. <br />
 *     这对于需要细粒度访问控制的应用来说是非常有用的.
 * </p>
 *
 * @since 1.0
 * @see JdbcRealm
 * @see AuthorizingRealm#getAuthorizationInfo(PrincipalCollection)
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
        User user = (User) principals.getPrimaryPrincipal();
        String username = user.getUsername();
        String desensitizedUsername = DesensitizeStrategyEnum.maskUsername(user.getUsername());
        LOGGER.debug("为用户 [{}] 加载角色和权限.", desensitizedUsername);

        // 加载用户的角色.
        Set<String> roles = this.userRepository.findRolesByUsername(username);
        LOGGER.info("用户 [{}] 加载的角色 [{}].", desensitizedUsername, roles);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(roles);

        // 加载用户的直接权限.
        Set<String> permissions = this.userRepository.findPermissionsByUsername(username);
        LOGGER.info("用户 [{}] 加载的直接权限 [{}].", desensitizedUsername, permissions);

        // 加载用户通过角色获得的间接权限.
        Set<String> rolePermissions = this.userRepository.findRolePermissionsByUsername(username);
        LOGGER.info("用户 [{}] 加载的角色权限 [{}].", desensitizedUsername, rolePermissions);

        // 合并直接权限和角色权限.
        permissions.addAll(rolePermissions);
        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();

        LOGGER.debug("正在认证用户 [{}].", DesensitizeStrategyEnum.maskUsername(username));

        if (username == null) {
            LOGGER.error("尝试使用空用户名进行认证.");
            throw new UnknownAccountException("用户名不存在.");
        }

        User user = this.userRepository.findUserByUsername(username);
        UserInfoValidator.validate(user);

        // Shiro 会自动验证密码是否匹配, 若不匹配会抛出 IncorrectCredentialsException 异常.
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(), // 数据库中的加密密码.
                new SimpleByteSource(user.getSalt()),
                getName() // 当前 Realm 的名称.
        );
    }
}