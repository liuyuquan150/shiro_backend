package indi.ly.crush.enums;

/**
 * <h2>角色</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public enum Role {
    /**
     * <p>
     *     系统管理员: 负责整个系统的管理和配置.
     * </p>
     */
    SYSTEM_ADMINISTRATOR,
    /**
     * <p>
     *     普通用户: 根据权限执行特定的操作.
     * </p>
     */
    USER,
    /**
     * <p>
     *     访客: 有限的访问权限, 通常仅能查看特定的公开信息, 没有修改数据的权限.
     * </p>
     */
    GUEST
}
