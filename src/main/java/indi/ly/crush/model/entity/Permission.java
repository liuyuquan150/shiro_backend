package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;

/**
 * <h2>权限</h2>
 * <p>
 *     权限相对于{@link Role 角色}而言则更为具体, 它定义了对系统中特定资源的访问和操作能力. <br />
 *     权限可以细分到非常具体的操作上, 比如{@code 读取文件}、{@code 编辑文件}、{@code 删除文件} 等.
 * </p>
 * <br />
 *
 * <h2>角色与权限的关系</h2>
 * <ul>
 *     <li>
 *         角色拥有权限: <br />
 *         通常情况下, 角色会被赋予一组权限. 这意味着拥有该角色的用户自动拥有该角色所对应的所有权限. <br />
 *         例如, 如果{@code 管理员角色}被赋予了{@code 删除文件}的权限, 那么所有{@code 管理员}都能删除文件.
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
 *         <th style="width: 50px; border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">id</th>
 *         <th style="width: 50px; border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">pid</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">permission</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">name</th>
 *         <th style="border: 2px solid white; text-align: left; padding: 8px; background-color: #555; color: white;">description</th>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">user:*</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">User Management</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Manage user accounts and information</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">2</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">user:create</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Create User</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Create a new user account</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">3</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">1</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">user:delete</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Delete User</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Delete an existing user account</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">report:*</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Report Viewing</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Access and view reports</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">5</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">4</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">report:download</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Download Report</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Download report files</td>
 *     </tr>
 *     <tr>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">6</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">0</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">dashboard:access</td>
 *         <td style="border: 2px solid white; text-align: left; padding: 8px;">Access Dashboard</td>
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
 *             我们有 {@code 3} 个角色, 系统管理员({@code SYSTEM_ADMINISTRATOR})拥有所有权限, 包括用户管理和报告查看;
 *             普通用户({@code USER})可以访问报告模块和仪表盘; 而访客({@code GUEST})仅能访问仪表盘.
 *         </li>
 *         <li>
 *             角色与权限关联: <br />
 *             通过 {@code t_role_permissions} 表关联角色与权限.
 *             例如, 系统管理员({@code SYSTEM_ADMINISTRATOR})角色有权执行所有操作, 包括用户管理和报告查看的所有相关操作.
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
@Table(name = "t_permission", schema = "shiro_backend")
public class Permission
        extends AbstractJpaExpansionEntity<Long> {
    @Serial
    private static final long serialVersionUID = 362498820763181265L;
    /**
     * <p>
     *     父权限的 {@code ID}, 用于建立权限之间的层级关系. <br />
     *     这样的设计允许创建一个权限树, 便于表示和管理具有内在层级或分组的权限结构.
     * </p>
     */
    @Column
    private Long pid;
    /**
     * <p>
     *     权限的唯一名称, 用于标识权限的目的或作用, 例如{@code 管理用户}、{@code 查看报告}等.
     * </p>
     */
    @Column(unique = true, nullable = false, length = 100)
    private String name;
    /**
     * <p>
     *     {@code Shiro} 通配符权限字符串. <br /> <br />
     *
     *     通配符权限格式 <br />
     *     通配符权限由一系列由冒号({@code :})分隔的部分组成, 每部分可以使用通配符({@code *})来匹配任意值. <br />
     *     {@code Shiro} 的通配符权限通常遵循以下格式: <br />
     *     {@code domain:action:instance}
     *     <ul>
     *         <li>{@code domain}: 权限的领域或类别, 如 {@code user} 或 {@code file}.</li>
     *         <li>{@code action}: 对领域执行的操作, 如 {@code read}、{@code write}、{@code create} 等.</li>
     *         <li>{@code instance}: 操作的实例, 通常是数据的唯一标识符, 如文件 {@code ID} 或用户 {@code ID}.</li>
     *     </ul>
     *
     *     使用通配符
     *     <ul>
     *         <li>
     *             星号({@code *}): <br />
     *             表示匹配任意值或一系列值. <br />
     *             例如, {@code user:*:123} 表示对 {@code ID} 为 {@code 123} 的用户执行任何操作的权限.
     *         </li>
     *     </ul>
     *
     *     通配符权限的使用例子
     *     <ol>
     *         <li>{@code user:view}: 允许查看用户.</li>
     *         <li>{@code user:view,edit}: 允许查看和编辑用户.</li>
     *         <li>{@code user:*}: 允许对用户执行任何操作.</li>
     *         <li>{@code user:*:123}: 允许对 {@code ID} 为 {@code 123} 的用户执行任何操作.</li>
     *         <li>{@code *:*:*} 或简写为 {@code *}: 允许对系统中的任何领域执行任何操作.</li>
     *     </ol>
     *
     *     层级权限 <br />
     *     {@code Shiro} 的通配符权限还支持层级权限, 即如果你有一个更高层级的权限, 你也自动拥有所有下级的权限. <br />
     *     例如:
     *     <ul>
     *         <li>如果你有 {@code user:*}, 你也有 {@code user:view}、{@code user:edit} 等所有子权限.</li>
     *         <li>如果你有 {@code user:view:*}, 你可以查看所有用户.</li>
     *     </ul>
     *     这种机制极大地简化了权限管理, 使得你不需要为用户明确指定每一个具体的权限.
     * </p>
     */
    @Column(name = "shiro_permission", unique = true, nullable = false)
    private String permission;
    /**
     * <p>
     *     权限的描述.
     * </p>
     */
    @Column
    private String description;

    public Permission() {
    }

    public Permission(String name, String permission, String description) {
        this.name = name;
        this.permission = permission;
        this.description = description;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
