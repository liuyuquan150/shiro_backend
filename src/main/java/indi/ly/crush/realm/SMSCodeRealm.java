package indi.ly.crush.realm;

import indi.ly.crush.model.entity.User;
import indi.ly.crush.repository.IUserRepository;
import indi.ly.crush.token.SMSCodeToken;
import indi.ly.crush.util.base.BaseStringUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h2>短信验证码安全域(短信验证码认证授权域)</h2>
 * <p>
 *     在认证过程中, 它将检查用户提供的手机号和短信验证码. <br />
 *     如果验证码有效, 它将使用用户信息创建 {@link AuthenticationInfo} 实例. <br /> <br />
 *
 *     在授权过程中, 它委托给 {@link UserRealm} 来提供所需的 {@link AuthorizationInfo}.
 * </p>
 * <br />
 *
 * <h2>为什么要自定义多个{@link Realm 安全域}?</h2>
 * <p>
 *     在构建身份验证和权限管理框架时, 我们可能会面对一个现实情况: 相关数据分散存储在不同的数据库或表中. <br />
 *     这种数据分散化的策略, 可以基于多种考量, 包括但不限于数据组织的需求、系统性能的优化以及安全性的提升. <br />
 *     为了有效管理和访问这些分布的数据源, 我们通常会选择为每种数据类型定制实现特定的 {@link Realm}. <br /> <br />
 *
 *     每个自定义的 {@link Realm} 都专门设计用来与一个特定的数据源交互, 从而优化数据访问流程. <br />
 *     这种做法不仅可以减轻单个数据库的访问压力, 还能提升整体系统的性能. <br />
 *     例如, 我们可以创建一个专门的 {@link UserRealm}, 它只负责与存储用户数据的数据库表进行通信. <br />
 *     这样, 所有与用户身份验证相关的操作都将由 {@link UserRealm} 来处理, 而无需其它 {@link Realm} 的介入. <br /> <br />
 *
 *     此外, 将数据访问逻辑划分到不同的 {@link Realm} 中, 也有助于提高代码的可读性和可维护性. <br />
 *     每个 {@link Realm} 都拥有清晰的职责划分, 使得逻辑更加模块化, 便于未来的扩展和维护.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class SMSCodeRealm
        extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSCodeRealm.class);
    private final IUserRepository userRepository;
    private final UserRealm userRealm;

    public SMSCodeRealm(IUserRepository userRepository, UserRealm userRealm) {
        this.userRepository = userRepository;
        this.userRealm = userRealm;
        // AuthenticatingRealm 162 行代码.
        super.setAuthenticationTokenClass(SMSCodeToken.class);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return this.userRealm.doGetAuthorizationInfo(principals);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        SMSCodeToken smsCodeToken = (SMSCodeToken) token;
        String phoneNumber = smsCodeToken.phoneNumber();
        String smsCode = smsCodeToken.code();

        LOGGER.debug("正在认证用户 [{}].", phoneNumber);

        // TODO 从 Redis 或其它缓存中获取的短信验证码进行比较.
        if (BaseStringUtil.notEquals(smsCode, "")) {
            throw new IncorrectCredentialsException("短信验证码已过期.");
        }

        User user = this.userRepository.findByPhoneNumber(phoneNumber);
        UserInfoValidator.validate(user);

        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                new SimpleByteSource(user.getSalt()),
                getName()
        );
    }
}
