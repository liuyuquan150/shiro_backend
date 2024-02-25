package indi.ly.crush.model.dto;

import indi.ly.crush.enums.Gender;
import indi.ly.crush.model.entity.User;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户数据传输对象</h2>
 *
 * @since 1.0
 * @author 云上的云
 */
public class UserDTO
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 362498820763181265L;
    /**
     * @see User#getUsername() User#username
     */
    private String username;
    /**
     * @see User#getGender() User#gender
     */
    private Gender gender;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
