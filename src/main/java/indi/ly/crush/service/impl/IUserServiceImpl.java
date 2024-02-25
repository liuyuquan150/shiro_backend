package indi.ly.crush.service.impl;

import indi.ly.crush.encryp.PasswordEncryption;
import indi.ly.crush.enums.Role;
import indi.ly.crush.ex.RegistrationFailedException;
import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import indi.ly.crush.model.from.UserRegistration;
import indi.ly.crush.realm.UserRealm;
import indi.ly.crush.repository.IRoleRepository;
import indi.ly.crush.repository.IUserRepository;
import indi.ly.crush.service.IUserService;
import lombok.NonNull;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

/**
 * <h2>用户服务实现</h2>
 * <h2>在 Java 程序中安全地处理密码等敏感信息的最佳实践</h2>
 * <p>
 *     {@link UsernamePasswordToken} 将密码被存储为字符数组 {@code char[]} 而不是字符串 {@code String}, 这是出于对安全性的考虑. <br />
 *     这是因为 {@link String} 是不可改变的, 它的内部值不能被覆盖,
 *     这意味着即使是一个空的字符串实例也可能在以后的时间(<em>例如内存转储</em>)中以内存的形式被访问, 这对密码等敏感信息来说是不利的. <br /> <br />
 *
 *      为了避免这种后期内存访问的可能性, 应用程序开发人员应该在使用{@link UsernamePasswordToken 令牌}进行登录尝试后, 始终调用 {@link UsernamePasswordToken#clear()} 方法:
 *      <pre>{@code
 *                  finally {
 *                      if (!token.isRememberMe()) {
 *                          token.clear();
 *                      }
 *                  }
 *      }</pre>
 *
 *      下述是更为详细的解释:
 *      <ol>
 *          <li>
 *              不可变的 {@link String}. <br /> <br />
 *
 *              在 {@code Java} 中, {@link String} 类型的对象是不可变的. <br />
 *              这意味着一旦一个 {@link String} 对象被创建, 它所包含的内容就不能被改变. <br />
 *              如果你试图修改 {@link String} 中的内容, 实际上会创建一个新的 {@link String} 对象, 而原始对象保持不变.
 *          </li>
 *          <li>
 *              安全隐患. <br /> <br />
 *
 *              对于密码等敏感信息, 使用 {@link String} 来存储存在安全隐患. <br />
 *              因为 {@link String} 对象不可变, 所以即使是你不再需要密码信息(<em>比如用户认证之后</em>), 密码原始内容仍然存储在内存中, 直到 {@code JVM} 进行垃圾回收. <br />
 *              在这段时间里, 如果有人能够访问到内存转储(<em>memory dump</em>), 就可能读取到这些敏感信息.
 *          </li>
 *          <li>
 *              使用 {@code char[]} 的优势. <br /> <br />
 *
 *              相比之下, 字符数组 {@code char[]} 是可变的. <br />
 *              这意味着你可以在使用完密码信息之后, 通过遍历数组并显式地将每个元素设置为零或其它值来 “清除” 这些信息, 从而减少敏感数据被意外泄露的风险.
 *          </li>
 *      </ol>
 *      因此, 为了避免敏感信息在内存中的潜在泄露, 开发者在使用完密码(<em>或其它敏感信息</em>)后, 应该立即清除存储这些信息的字符数组. <br />
 *      这是一种防御性编程的策略, 旨在提高应用程序的安全性. <br /> <br />
 *
 *      有关更多信息, 请参阅 <a href="http://java.sun.com/j2se/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx">Java Cryptography Extension Reference Guide —————— Java 加密扩展参考指南</a>.
 * </p>
 *
 * @since 1.0
 * @see UserRealm#doGetAuthenticationInfo(AuthenticationToken)
 * @author 云上的云
 * @formatter:off
 */
@Service
public class IUserServiceImpl
        implements IUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IUserServiceImpl.class);
    private final IUserRepository userRepositoryImpl;
    private final IRoleRepository roleRepositoryImpl;
    private final HashedCredentialsMatcher hashedCredentialsMatcher;
    private final TransactionTemplate transactionTemplate;

    public IUserServiceImpl(
            IUserRepository userRepositoryImpl,
            IRoleRepository roleRepositoryImpl,
            HashedCredentialsMatcher hashedCredentialsMatcher,
            TransactionTemplate transactionTemplate
    ) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.roleRepositoryImpl = roleRepositoryImpl;
        this.hashedCredentialsMatcher = hashedCredentialsMatcher;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void registerUserAndAssignDefaultRole(@NonNull UserRegistration userRegistration) {
        User user = new User(userRegistration.getUsername());

        Optional<User> userOptional = this.userRepositoryImpl.findOne(Example.of(user));
        if (userOptional.isPresent()) {
            throw new RegistrationFailedException("注册失败, 用户名已占用.");
        }

        String salt = PasswordEncryption.generateSalt();
        user.setSalt(salt); // 保存盐值到用户记录中.

        String hashAlgorithmName = this.hashedCredentialsMatcher.getHashAlgorithmName();
        int hashIterations = this.hashedCredentialsMatcher.getHashIterations();

        String newPassword = PasswordEncryption.encryptPassword(hashAlgorithmName, userRegistration.getPassword(), salt, hashIterations);
        user.setPassword(newPassword); // 保存加密之后的密码到用户记录中.

        this.transactionTemplate.execute(status -> {
            long userId = this.userRepositoryImpl.saveAndFlush(user).getId();                                               // 添加用户.
            this.roleRepositoryImpl.assignRoleToUser(userId, (long) Role.SYSTEM_ADMINISTRATOR.ordinal() + 1);               // 为用户分配角色.
            return null;
        });
    }

    @Override
    public @NonNull User login(@NonNull UserCredentials userCredentials) {
        UsernamePasswordToken token = new UsernamePasswordToken(userCredentials.getUsername(), userCredentials.getPassword(), userCredentials.isRememberMe());
        Subject subject = SecurityUtils.getSubject();
        try {
            /*
                当调用 Subject.login() 进行登录时, Shiro 会使用配置的 Realm 执行认证逻辑.
                如果认证成功, Realm 会返回一个 AuthenticationInfo 实例, 其中包含了 Principal 和其它认证信息.
                Principal 通常是用户的标识信息, 如用户名或用户对象, 具体取决于 Realm 的实现.
             */
            subject.login(token);

            LOGGER.info("用户 [{}] 认证成功.", token.getUsername());

            // 确保 Realm doGetAuthenticationInfo 方法返回的是 User 类型.
            return (User) subject.getPrincipal();
        } catch (IncorrectCredentialsException e) {     // 密码不一致.
            LOGGER.error("{} =======> {}", e.getClass(), e.getMessage());
            throw new IncorrectCredentialsException("密码错误, 登录失败.");
        } finally {
            if (!token.isRememberMe()) {
                // 在登录信息不再需要时立即清除, 以减少数据泄露的风险, 消除以后访问内存的可能性. "http://java.sun.com/j2se/1.5.0/docs/guide/security/jce/JCERefGuide.html#PBEEx"
                token.clear();
            }
        }
    }
}
