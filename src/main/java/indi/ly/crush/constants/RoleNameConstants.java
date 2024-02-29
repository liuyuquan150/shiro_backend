package indi.ly.crush.constants;

import indi.ly.crush.enums.Role;

/**
 * <h2>角色名称常量类</h2>
 * <p>
 *     此类中定义的角色名称常量字符串必须与 {@link Role} 类中声明的枚举对象的名称保持一致.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public final class RoleNameConstants {
    /**
     * @see Role#SYSTEM_ADMINISTRATOR
     */
    public static final String SYSTEM_ADMINISTRATOR = "SYSTEM_ADMINISTRATOR";
    /**
     * @see Role#GUEST
     */
    public static final String GUEST = "GUEST";
    private RoleNameConstants() {}
}
