package indi.ly.crush.model.from;

import indi.ly.crush.enums.LoginType;
import indi.ly.crush.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户凭证</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Getter
@Setter
public class UserCredentials
        implements Serializable {
    @Serial
    private static final long serialVersionUID = -6849794470754667710L;
    /**
     * @see User#getUsername() User#username
     */
    private String username;
    /**
     * @see User#getPassword() User#password
     */
    private String password;
    /**
     * @see User#getPhoneNumber() User#phoneNumber
     */
    private String phoneNumber;
    /**
     * <p>
     *     短信验证码.
     * </p>
     */
    private String code;
    /**
     * <p>
     *     记住我.
     * </p>
     */
    private boolean rememberMe;
    /**
     * <p>
     *     登录类型.
     * </p>
     */
    private LoginType loginType;
}
