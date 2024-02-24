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
 * <h2>授权须知</h2>
 * <p>
 *     一个用户可能不具备某个指定的角色, 但它是有可能拥有这个指定角色的权限的. <br />
 *     这是一种设计, 这种设计提供了更高的灵活性和细粒度的控制, 允许系统管理员对用户的权限进行精细调整. <br /> <br />
 *
 *     举个例子:
 *     <ul>
 *         <li>
 *             假设 “编辑文章” 的权限通常只赋予 “编辑” 角色.
 *         </li>
 *         <li>
 *             但是, 可能有一个特例用户(不是 “编辑” 角色的成员), 因为某些原因, 他需要获得 “编辑文章” 的权限.
 *         </li>
 *         <li>
 *             在这种情况下, 系统管理员可以直接给这个用户赋予 “编辑文章” 的权限, 而不需要将他归入 “编辑” 角色.
 *         </li>
 *     </ul>
 *     这种方法的优点是提供了极大的灵活性, 使得权限管理可以非常精确地控制每个用户能做什么, 而不是仅仅依赖角色这一粗糙的划分. <br />
 *     这对于需要细粒度访问控制的应用来说是非常有用的.
 * </p>
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
        User user = (User) principals.getPrimaryPrincipal();
        String username = user.getUsername();
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

        // Shiro 会自动验证密码是否匹配, 若不匹配会抛出 IncorrectCredentialsException 异常.
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(), // 数据库中的加密密码.
                ByteSource.Util.bytes(user.getSalt()),
                getName() // 当前 Realm 的名称.
        );
    }
}