package indi.ly.crush.model.from;

import indi.ly.crush.enums.Role;
import indi.ly.crush.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户注册</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Getter
@Setter
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
    /**
     * @see User#getPhoneNumber() User#phoneNumber
     */
    private String phoneNumber;
    /**
     * @see Role
     * @see indi.ly.crush.model.entity.Role
     */
    private Role role;

    public UserRegistration() {
    }

    public UserRegistration(String username, String password, String phoneNumber, Role role) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
