package indi.ly.crush.service.impl;

import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import indi.ly.crush.realm.UserRealm;
import indi.ly.crush.service.IUserService;
import lombok.NonNull;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

/**
 * <h2>用户服务实现</h2>
 *
 * @since 1.0
 * @see UserRealm
 * @see UserRealm#doGetAuthenticationInfo(AuthenticationToken)
 * @author 云上的云
 * @formatter:off
 */
@Service
public class IUserServiceImpl
        implements IUserService {
    @Override
    public @NonNull User login(@NonNull UserCredentials userCredentials) {
        UsernamePasswordToken token = new UsernamePasswordToken(userCredentials.getUsername(), userCredentials.getPassword(), userCredentials.isRememberMe());
        Subject subject = SecurityUtils.getSubject();
        /*
            当调用 Subject.login() 进行登录时, Shiro 会使用配置的 Realm 执行认证逻辑.
            如果认证成功, Realm 会返回一个 AuthenticationInfo 实例, 其中包含了 Principal 和其它认证信息.
            Principal 通常是用户的标识信息, 如用户名或用户对象, 具体取决于 Realm 的实现.
         */
        subject.login(token);
        // 确保 Realm doGetAuthenticationInfo 方法返回的是 User 类型.
        return (User) subject.getPrincipal();
    }
}
