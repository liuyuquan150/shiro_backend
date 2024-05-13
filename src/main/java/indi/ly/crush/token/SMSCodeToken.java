package indi.ly.crush.token;

import indi.ly.crush.model.entity.User;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * <h2>短信验证码认证令牌</h2>
 * <p>
 *     在短信验证码认证登录的场景中, {@code Shiro} 提供的 {@link UsernamePasswordToken} 无法传递额外的参数(手机号码跟短信验证码)给认证器,
 *     因此创建 {@code 1} 个自定义的 {@link AuthenticationToken} 实现来满足需求.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class SMSCodeToken
        implements RememberMeAuthenticationToken {
    /**
     * <p>
     *     手机号码, 作为用户的身份标识.
     * </p>
     *
     * @see User#getPhoneNumber() User#phoneNumber
     */
    private final String phoneNumber;
    /**
     * <p>
     *     短信验证码, 作为用户的凭证.
     * </p>
     */
    private final String code;
    /**
     * <p>
     *     是否启用{@code 记住我}.
     * </p>
     */
    private final boolean rememberMe;

    public SMSCodeToken(String phoneNumber, String code, boolean rememberMe) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.rememberMe = rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber();
    }

    @Override
    public Object getCredentials() {
        return code();
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }

    public String phoneNumber() {
        return phoneNumber;
    }

    public String code() {
        return code;
    }

    @Override
    public String toString() {
        return "SMSCodeToken{" +
               "phoneNumber='" + phoneNumber + '\'' +
               ", code='" + code + '\'' +
               ", rememberMe=" + rememberMe +
               '}';
    }
}
