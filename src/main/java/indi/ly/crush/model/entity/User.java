package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;
import indi.ly.crush.enums.Gender;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>用户</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Entity
@Table(name = "t_user", schema = "shiro_backend")
public class User
		extends AbstractJpaExpansionEntity<Long> {
	@Serial
	private static final long serialVersionUID = 362498820763181265L;
	/**
	 * <p>
	 *     用户名.
	 * </p>
	 */
	@Column(unique = true, nullable = false, length = 64)
	private String username;
	/**
	 * <p>
	 *     用户密码.
	 * </p>
	 */
	@Column(nullable = false, length = 64)
	private String password;
	/**
	 * <p>
	 *     盐. <br /> <br />
	 *
	 *     在哈希{@link #password 密码}之前向{@link #password 密码}添加一个随机值(<em>即盐</em>),
	 *     这样做可以大大增加破解存储的密码的难度, 因为即使两个用户具有相同的密码, 它们的哈希值也会因为盐的不同而不同.
	 * </p>
	 */
	@Column(nullable = false, length = 64)
	private String salt;
	@Column(columnDefinition = "TINYINT(1) UNSIGNED NOT NULL COMMENT '性别：0-女  1-男 2-私密'")
	@Enumerated(value = EnumType.ORDINAL)
	private Gender gender = Gender.UNKNOWN;
	/**
	 * <p>
	 *     角色.
	 * </p>
	 */
	@ManyToMany(fetch = FetchType.EAGER)	// 指定具有多对多多重性的多值关联.
	@JoinTable(
			name = "t_user_role",
			joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
	)
	private List<Role> roles;
	/**
	 * <p>
	 *     是否锁定: 0-否 1-是.
	 * </p>
	 */
	@Column(columnDefinition = "TINYINT(1) UNSIGNED NOT NULL COMMENT '是否锁定: 0-否 1-是'")
	private Boolean locked = false;
	/**
	 * <p>
	 *     是否可用: 0-否 1-是.
	 * </p>
	 */
	@Column(columnDefinition = "TINYINT(1) UNSIGNED NOT NULL COMMENT '是否可用: 0-否 1-是'")
	private Boolean enabled = true;
	/**
	 * <p>
	 *     最后登录 IP.
	 * </p>
	 */
	@Column(length = 64)
	private String lastLoginIp;
	/**
	 * <p>
	 *     最后登录时间.
	 * </p>
	 */
	@Column
	private LocalDateTime lastLoginTime;

	public User() {}

	public User(String username) {
		this.username = username;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
}