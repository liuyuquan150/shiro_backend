package indi.ly.crush.enums;

import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <h2>权限类型</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public enum PermissionType {
    /**
     * <p>
     *     读取权限: 允许用户读取或查看信息. 适用于需要保护数据不被未授权查看的场景.
     * </p>
     */
    READ("允许读取或查看信息."),
    /**
     * <p>
     *     写入权限: 允许用户创建或修改信息. 对于需要用户输入或更新数据的功能非常重要.
     * </p>
     */
    WRITE("允许创建或修改信息."),
    /**
     * <p>
     *     删除权限: 允许用户删除信息. 用于控制对删除操作的访问, 以防止数据被误删或未经授权的删除.
     * </p>
     */
    DELETE("允许删除信息."),
    /**
     * <p>
     *     执行权限: 允许用户执行或运行特定操作. 通常用于控制对应用程序中执行操作的权限, 如启动或停止服务.
     * </p>
     */
    EXECUTE( "允许执行或运行特定操作."),
    /**
     * <p>
     *     下载权限: 允许用户下载内容. 适用于控制对文件下载操作的访问, 确保只有授权用户能下载敏感或受保护的内容.
     * </p>
     */
    DOWNLOAD("允许下载内容."),
    /**
     * <p>
     *     上传权限: 允许用户上传内容. 对于需要用户上传文件或数据到服务器的功能来说这, 这一权限至关重要.
     * </p>
     */
    UPLOAD("允许上传内容."),
    /**
     * <p>
     *     配置权限: 允许用户配置或设置系统. 通常用于提供对系统设置或配置操作的访问权限, 如修改配置文件或系统偏好设置.
     * </p>
     */
    CONFIGURE("允许配置或设置系统."),
    /**
     * <p>
     *     管理员权限: 拥有全部管理权限, 可以进行全面的访问和操作. 这一权限通常赋予系统管理员, 允许他们执行包括但不限于以上权限的任何操作.
     * </p>
     */
    ADMIN("拥有全部管理权限, 可以进行全面的访问和操作."),
    /**
     * <p>
     *     模块访问权限: 允许访问特定模块或区域的权限. 用于控制用户对应用程序的不同功能区的访问, 如 “用户管理模块”、“报告模块” 等.
     * </p>
     */
    MODULE("允许访问特定模块或区域的权限. 用于控制用户对应用程序的不同功能区的访问, 如 “用户管理模块”、“报告模块” 等."),
    /**
     * <p>
     *     按钮操作权限: 允许与界面上特定按钮或操作进行交互的权限. 用于细粒度控制, 如允许用户执行特定操作(例如添加或删除记录).
     * </p>
     */
    BUTTON("允许与界面上特定按钮或操作进行交互的权限. 用于细粒度控制, 如允许用户执行特定操作(例如添加或删除记录).");

    private final String description;

    PermissionType(@NonNull String description) {
        this.description = Objects.requireNonNull(description, "");
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return StringUtils.capitalize(this.name().toLowerCase()) + ": " + this.description;
    }
}
