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
            long userId = this.userRepositoryImpl.saveAndFlush(user).getId();                           // 添加用户.
            this.roleRepositoryImpl.assignRoleToUser(userId, (long) Role.USER.ordinal());               // 为用户分配角色.
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
