package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;
import indi.ly.crush.enums.Gender;
import lombok.Getter;
import lombok.Setter;

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
 *             例如, 一个用户可能同时是{@code 项目经理}和{@code 质量保证分析师}, 每个角色都有其独特的权限.
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
@Getter
@Setter

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
	 *     手机号码.
	 * </p>
	 */
	@Column(nullable = false, length = 11)
	private String phoneNumber;
	/**
	 * <p>
	 *     盐. <br /> <br />
	 *
	 *     在哈希{@link #password 密码}之前向{@link #password 密码}添加一个随机值(盐),
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
	 *     用户拥有的所有角色.
	 * </p>
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)	// 指定具有多对多多重性的多值关联.
	@JoinTable(
			name = "t_user_role",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private List<Role> roles;
	/**
	 * <p>
	 *     用户直接拥有的所有权限. <br /> <br />
	 *
	 *     有时我们会使用用户名直接匹配权限而不是先匹配角色再匹配权限, 这取决于系统的设计和需求. <br />
	 *     下面是一些为什么可能会采用这种设计的原因:
	 *     <ol>
	 *         <li>
	 *             直接权限分配: <br />
	 *             在某些系统中, 除了通过角色间接赋予权限之外, 还允许直接给用户分配权限. <br />
	 *             这种设计提供了更灵活的权限管理方式, 允许对特定用户进行精细化的权限控制.
	 *         </li>
	 *         <li>
	 *             简化的权限模型: <br />
	 *             直接通过用户名匹配权限可以简化权限管理模型, 尤其是在权限结构比较简单或者用户量不大的系统中. <br />
	 *             这种方式减少了角色管理的复杂度, 使得权限管理更为直观.
	 *         </li>
	 *         <li>
	 *             性能考虑: <br />
	 *             在某些情况下, 如果用户与权限之间的关系比用户与角色、角色与权限之间的关系更为直接, 直接查询权限可能会更高效. <br />
	 *             特别是在用户-权限关系相对固定, 而且查询频繁的场景下.
	 *         </li>
	 *         <li>
	 *             特殊权限需求: <br />
	 *             某些用户可能需要一些特殊的权限, 这些权限不属于任何角色. <br />
	 *             直接分配权限可以确保这些用户获得所需的特殊权限, 而不必为他们创建独特的角色.
	 *         </li>
	 *         <li>
	 *             角色与权限混合使用: <br />
	 *             在实际应用中, 可能同时使用角色和直接权限两种方式. <br />
	 *             例如, 基本权限通过角色分配, 而特殊权限直接分配给用户. 这种混合方式提供了更大的灵活性和精确控制.
	 *         </li>
	 *     </ol>
	 *     尽管直接通过用户名匹配权限在某些情况下有其优势,
	 *     但在复杂的系统中, 通过角色管理权限通常是更优的选择, 因为它可以减少管理的复杂性，提高权限变更的效率, 并且使权限分配更加清晰和可管理. <br />
	 * </p>
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "t_user_permission",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
	)
	private List<Permission> permissions;
	/**
	 * <p>
	 *     是否锁定: {@code 0}-否 {@code 1}-是.
	 * </p>
	 */
	@Column(columnDefinition = "TINYINT(1) UNSIGNED NOT NULL COMMENT '是否锁定: 0-否 1-是'")
	private Boolean locked = false;
	/**
	 * <p>
	 *     是否可用: {@code 0}-否 {@code 1}-是.
	 * </p>
	 */
	@Column(columnDefinition = "TINYINT(1) UNSIGNED NOT NULL COMMENT '是否可用: 0-否 1-是'")
	private Boolean enabled = true;
	/**
	 * <p>
	 *     最后登录 {@code IP}.
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

	public User(Long id, String username, String password, String salt) {
		super.setId(id);
		this.username = username;
		this.password = password;
		this.salt = salt;
	}
}