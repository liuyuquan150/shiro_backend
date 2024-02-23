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
 * <h2 style="color: white;">角色表(t_role)</h2>
 * <table style="border-collapse: collapse; width: 100%;">
 *     <tr>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">id</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">name</th>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">SYSTEM_ADMINISTRATOR</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">USER</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">3</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">GUEST</td>
 *     </tr>
 * </table>
 *
 * <h2 style="color: white;">权限表(t_permission)</h2>
 * <table style="border-collapse: collapse; width: 100%;">
 *     <tr>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">id</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">pid</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">url</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">name</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">type</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">description</th>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">null</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">User Management</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">MODULE</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Manage user accounts and information</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">/users/create</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Create User</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">BUTTON</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Create a new user account</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">3</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">/users/delete</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Delete User</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">BUTTON</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Delete an existing user account</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">null</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Report Viewing</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">MODULE</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Access and view reports</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">5</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">/reports/download</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Download Report</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">BUTTON</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Download report files</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">6</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">/dashboard</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Access Dashboard</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">BUTTON</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Access the application dashboard</td>
 *     </tr>
 * </table>
 *
 * <h2 style="color: white;">角色与权限关联表(t_role_permissions)</h2>
 * <table style="border-collapse: collapse; width: 100%;">
 *     <tr>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">role_id</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">permissions_id</th>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">3</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">5</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">6</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">6</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">3</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">6</td>
 *     </tr>
 * </table>
 * <p>
 *     解释
 *     <ul>
 *         <li>
 *             角色: <br />
 *             我们有三个角色, 系统管理员(SYSTEM_ADMINISTRATOR)拥有所有权限, 包括用户管理和报告查看;
 *             普通用户(USER)可以访问报告模块和仪表盘; 而访客(GUEST)仅能访问仪表盘.
 *         </li>
 *         <li>
 *             权限: <br />
 *             权限分为两种类型, 模块(MODULE)和按钮(BUTTON). <br />
 *             模块类型的权限如 “用户管理” 和 “报告查看” 不直接关联到 {@code URL}, 表示一组功能或操作. <br />
 *             按钮类型的权限如 “创建用户”、“删除用户” 和 “下载报告” 等关联到具体的 {@code URL}, 表示单一的操作.
 *         </li>
 *         <li>
 *             角色与权限关联: <br />
 *             通过 {@code t_role_permissions} 表关联角色与权限.
 *             例如, 系统管理员(SYSTEM_ADMINISTRATOR)角色有权执行所有操作, 包括用户管理和报告查看的所有相关操作.
 *         </li>
 *     </ul>
 *     这个示例展示了如何在实际应用中设置角色和权限的数据结构, 以及如何通过角色来间接管理用户的权限. <br />
 *     这种模型可以根据具体的应用需求进行调整和扩展.
 * </p>
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