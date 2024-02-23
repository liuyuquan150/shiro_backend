package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;

import javax.persistence.*;
import java.io.Serial;
import java.util.List;

/**
 * <h2>角色</h2>
 * <p>
 *     角色通常指的是用户组的标识, 它表示一组用户的集合. 角色用于对一类具有相似访问和操作需求的用户进行分组. <br />
 *     例如, 一个 {@link indi.ly.crush.enums.Role#SYSTEM_ADMINISTRATOR 管理员} 角色可能包括对系统设置进行更改的能力,
 *     而一个 {@link indi.ly.crush.enums.Role#USER 用户} 角色可能只能查看内容而不能进行任何更改.
 * </p>
 * <br />
 *
 * <h2>角色与权限的关系</h2>
 * <ul>
 *     <li>
 *         角色拥有权限: <br />
 *         通常情况下, 角色会被赋予一组权限. 这意味着拥有该角色的用户自动拥有该角色所对应的所有权限. <br />
 *         例如, 如果 “管理员” 角色被赋予了 “删除文件” 的权限, 那么所有 “管理员” 都能删除文件.
 *     </li>
 *     <li>
 *         直接赋予权限: <br />
 *         然而, 系统的设计也可以允许直接向用户赋予权限, 而不是通过角色间接赋予. <br />
 *         这就是上述句子的含义. 即使某个用户可能没有被赋予特定的角色, 但他们仍然可以直接被赋予该角色所拥有的某些权限.
 *     </li>
 * </ul>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Entity
@Table(name = "t_role", schema = "shiro_backend")
public class Role
		extends AbstractJpaExpansionEntity<Long> {
	@Serial
	private static final long serialVersionUID = 362498820763181265L;
	/**
	 * <p>
	 *     角色名.
	 * </p>
	 */
	@Column(nullable = false, length = 128)
	private String name;
	/**
	 * <p>
	 *     角色对应的所有权限.
	 * </p>
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name = "t_role_permissions",
			joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "permissions_id", referencedColumnName = "id")
	)
	private List<Permission> permissions;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}
}