package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;
import indi.ly.crush.enums.Gender;

import javax.persistence.*;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>用户</h2>
 * <h2>多角色模型</h2>
 * <p>
 *     一个用户可以有多种角色, 这是许多权限管理系统中的一个常见特性. <br />
 *     这种设计允许更灵活和细粒度的访问控制, 因为它可以根据用户的不同角色组合来分配不同的权限集合. <br />
 *     这样, 系统就能够更准确地反映用户在现实世界中可能拥有的多重身份或职责. <br /> <br />
 *
 *     多角色模型的优势:
 *     <ol>
 *         <li>
 *             灵活性: <br />
 *             通过为单一用户分配多个角色, 可以灵活地组合不同的权限集, 以适应用户的具体需求. <br />
 *             例如, 一个用户可能同时是 “项目经理” 和 “质量保证分析师”, 每个角色都有其独特的权限.
 *         </li>
 *         <li>
 *             简化管理: <br />
 *             在大型系统中, 用户可能需要根据他们在组织中的不同职责访问不同的资源. <br />
 *             通过赋予他们多个角色, 可以更容易地管理这些权限, 而不是为每个用户手动配置每项权限.
 *         </li>
 *         <li>
 *             易于扩展: <br />
 *             随着组织的发展, 新的角色和权限可能会被引入. <br />
 *             在多角色模型中, 可以通过简单地为用户分配新角色来扩展他们的权限, 而不需要重新配置现有的权限结构.
 *         </li>
 *         <li>
 *             提高安全性: <br />
 *             通过细粒度的角色和权限分配, 可以限制用户访问仅对其工作必要的资源, 遵循最小权限原则, 从而增加系统的安全性.
 *         </li>
 *     </ol>
 *
 *     实现多角色模型:
 *     <ol>
 *         <li>
 *             角色继承: <br />
 *             在一些系统中, 角色可以有层级关系, 即一个角色可以继承另一个角色的权限. <br />
 *             这种设计可以进一步增加权限管理的灵活性和效率.
 *         </li>
 *         <li>
 *             角色与权限的映射: <br />
 *             需要有一个清晰的映射机制, 定义哪些权限与哪些角色相关联. <br />
 *             这可能是通过数据库表、配置文件或其它机制实现的.
 *         </li>
 *         <li>
 *             动态角色分配: <br />
 *             在一些高级用例中, 角色可以根据上下文动态分配给用户. <br />
 *             例如, 基于用户的位置、时间或特定事件.
 *         </li>
 *     </ol>
 *     总的来说, 允许用户具有多种角色是提供复杂和灵活访问控制策略的有效方法. <br />
 *     它不仅可以提高用户管理的灵活性和效率, 还可以通过精确控制资源访问来增强系统的安全性.
 * </p>
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
	@ManyToMany(fetch = FetchType.LAZY)	// 指定具有多对多多重性的多值关联.
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