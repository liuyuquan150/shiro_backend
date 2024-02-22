package indi.ly.crush.model.from;

import indi.ly.crush.model.entity.User;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户注册</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class UserRegistration
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

    public UserRegistration() {
    }

    public UserRegistration(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
}
