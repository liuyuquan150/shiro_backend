package indi.ly.crush.enums;

/**
 * <h2>登录类型</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public enum LoginType {
    /**
     * <p>
     *     通过{@code 用户名密码}登录.
     * </p>
     */
    USERNAME_PASSWORD(),
    /**
     * <p>
     *     通过{@code 手机号短信验证码}登录.
     * </p>
     */
    SMS_CODE()
}
