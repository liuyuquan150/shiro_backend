package indi.ly.crush.model.from;

import indi.ly.crush.model.entity.User;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户凭证</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
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
     * <p>
     *     记住我.
     * </p>
     */
    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
