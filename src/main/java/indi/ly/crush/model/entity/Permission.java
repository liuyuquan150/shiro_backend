package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;
import indi.ly.crush.enums.PermissionType;

import javax.persistence.*;
import java.io.Serial;

/**
 * <h2>权限</h2>
 * <p>
 *     权限相对于{@link Role 角色}而言则更为具体, 它定义了对系统中特定资源的访问和操作能力. <br />
 *     权限可以细分到非常具体的操作上, 比如 “读取文件”、“编辑文件”、“删除文件” 等.
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
     *     权限相关联的 {@code URL}, 这通常指的是应用程序中受保护资源的具体路径或模式. <br />
     *     通过将权限与 {@code URL} 路径关联, 可以在应用程序中实现基于路径的访问控制. <br /> <br />
     *
     *     在权限管理系统中, 将 {@code url} 字段设置为可以为空({@code nullable = true})的原因可能是出于以下几个考虑:
     *     <ol>
     *         <li>
     *              层级和组织结构权限: <br />
     *              在某些情况下, 权限被组织成层级或树状结构, 顶层权限可能代表对一组功能的访问权, 而不是指向特定的 URL.<br />
     *              这种情况下, 只有叶节点权限可能会与具体的 {@code URL} 关联, 而中间节点则更多地代表权限的逻辑分组, 而不直接关联到 {@code URL}.
     *         </li>
     *     </ol>
     *
     *     让我们通过一个具体的例子来说明为什么 {@code url} 字段在权限管理系统中可以为空:
     *     <ul>
     *         <li>
     *             文章管理权限:
     *             <ul>
     *                 <li>id: {@code 1}</li>
     *                 <li>pid: {@code null}(表示这是一个顶级权限)</li>
     *                 <li>url: {@code null}(因为这个权限是一个功能模块的通用权限, 不对应特定的 URL)</li>
     *                 <li>name: {@code Article Management}</li>
     *                 <li>type: {@code MODULE}</li>
     *             </ul>
     *         </li>
     *         <li>
     *             添加文章权限(<em>文章管理权限的子权限</em>):
     *             <ul>
     *                 <li>id: {@code 2}</li>
     *                 <li>pid: {@code 1}(表示这是文章管理权限的子权限)</li>
     *                 <li>url: {@code /articles/add}(对应添加文章的 URL)</li>
     *                 <li>name: {@code Add Article}</li>
     *                 <li>type: {@code BUTTON}</li>
     *             </ul>
     *         </li>
     *     </ul>
     *     在这个例子中, 文章管理权限没有直接关联到一个具体的 {@code URL}, 因为它代表了一个更广泛的功能模块, 而不是单一的操作或资源. <br />
     *     这些权限下可能有多个子权限, 每个子权限才具体对应到某个操作的 {@code URL}, 比如添加文章权限对应到添加文章的 {@code URL}. <br /> <br />
     *
     *     这种设计允许我们在权限模型中构建层级结构, 使得顶级权限(如模块级权限)可以不直接关联到具体的 {@code URL}. <br />
     *     这提供了更大的灵活性, 使得系统能够支持各种不同类型的权限定义, 从而适应复杂的业务需求和未来的扩展. <br />
     *     同时, 它还简化了权限的管理, 因为可以通过管理顶级权限来间接控制对一组相关操作的访问. <br /> <br />
     *
     *     在这种层级权限模型中, 顶级权限作为父权限, 包含了一组子权限. <br />
     *     授予某个用户或角色一个顶级权限, 意味着同时授予了该顶级权限下所有子权限的访问能力. <br />
     *     这样的设计使得权限管理更加集中和高效, 因为:
     *     <ul>
     *         <li>只需要操作较少的权限对象就能控制对多个操作或资源的访问.</li>
     *         <li>可以轻松地为用户或角色配置权限, 而不需要逐一指定每个具体操作的权限.</li>
     *         <li>简化了权限的继承和传播, 特别是当需要对权限结构进行调整时.</li>
     *     </ul>
     *
     *     假设在一个内容管理系统中, 我们有以下层级权限结构:
     *     <ul>
     *         <li>
     *             内容管理(顶级权限)
     *             <ul>
     *                 <li>查看文章</li>
     *                 <li>添加文章</li>
     *                 <li>编辑文章</li>
     *                 <li>删除文章</li>
     *             </ul>
     *         </li>
     *     </ul>
     *     在这个例子中, 如果某个用户被赋予了 “内容管理” 的顶级权限, 那么这个用户就自动拥有了查看、添加、编辑和删除文章的权限. <br />
     *     无需逐一为用户配置 “查看文章”、“添加文章” 等子权限, 大大简化了权限的配置和管理工作. <br /> <br />
     *
     *     优势
     *     <ol>
     *         <li>灵活性: 在需要调整权限设置时, 只需修改顶级权限下的子权限配置, 而无需重新为每个用户或角色配置权限.</li>
     *         <li>易于管理: 权限的层级结构使得理解和管理权限变得更加直观和简单.</li>
     *         <li>扩展性: 随着系统功能的扩展, 可以方便地通过添加新的子权限到相应的顶级权限下来扩展权限模型.</li>
     *     </ol>
     * </p>
     */
    @Column
    private String url;
    /**
     * <p>
     *     权限的名称, 用于标识权限的目的或作用, 例如 “管理用户”、“查看报告” 等.
     * </p>
     */
    @Column(nullable = false, length = 100)
    private String name;
    /**
     * <p>
     *     权限的类型, 用于进一步细分权限或指定权限的种类, 比如 “读”、“写”、“执行”、“模块”、“按钮” 等.
     * </p>
     */
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PermissionType type;
    /**
     * <p>
     *     权限的描述.
     * </p>
     */
    @Column
    private String description;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
