package indi.ly.crush.model.vo;

import indi.ly.crush.enums.Gender;
import indi.ly.crush.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h2>用户视图对象</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Getter
@Setter
public class UserVO
        implements Serializable {
    @Serial
    private static final long serialVersionUID = 362498820763181265L;
    /**
     * @see User#getId() User#id
     */
    private Long id;
    /**
     * @see User#getUsername() User#username
     */
    private String username;
    /**
     * @see User#getGender() User#gender
     */
    private Gender gender;
}
