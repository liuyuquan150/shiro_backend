package indi.ly.crush.service;

import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import indi.ly.crush.model.from.UserRegistration;
import lombok.NonNull;

/**
 * <h2>账户服务接口定义</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IAccountService {
    /**
     * <p>
     *     注册用户并分配默认角色. <br /> <br />
     *
     *     请你注意, 在用户注册时需使用加密密码.
     * </p>
     *
     * @param userRegistration 包含注册信息的的实例.
     */
    void registerUserAndAssignDefaultRole(@NonNull UserRegistration userRegistration);
    /**
     * <p>
     *     用户登录操作. <br /> <br />
     *
     *     该方法接受用户凭证作为参数, 进行身份认证. <br />
     *     如果认证成功, 返回一个包含用户信息的 {@link User} 对象. <br /> <br />
     *
     *     请你注意,
     *     用户登录进行密码验证时所使用到的算法和盐值、以及散列次数要与用户注册时所使用到的算法和盐值、以及散列次数保持一致.
     * </p>
     *
     * @param userCredentials 包含用户登录信息的实例.
     * @return 一个 {@link User} 对象, 包含用户的详细信息.
     */
    @NonNull User login(@NonNull UserCredentials userCredentials);
}
