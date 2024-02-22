package indi.ly.crush.service;

import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import lombok.NonNull;

/**
 * <h2>用户服务接口定义</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IUserService {
    /**
     * <p>
     *     用户登录操作. <br /> <br />
     *
     *     该方法接受用户凭证作为参数, 进行身份认证. <br />
     *     如果认证成功, 返回一个包含用户信息的 {@link User} 对象.
     * </p>
     *
     * @param userCredentials 包含用户登录信息的用户凭证.
     * @return 一个 {@link User} 对象, 包含用户的详细信息.
     */
    @NonNull User login(@NonNull UserCredentials userCredentials);
}
