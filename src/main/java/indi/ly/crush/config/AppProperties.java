package indi.ly.crush.config;

import indi.ly.crush.model.entity.Permission;
import indi.ly.crush.model.entity.Role;
import indi.ly.crush.util.base.BaseStringUtil;
import lombok.NonNull;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <h2>应用程序属性</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    /**
     * <p>
     *     权限列表.
     * </p>
     */
    private List<PermissionConfig> permissions = new LinkedList<>();
    /**
     * <p>
     *     角色列表.
     * </p>
     */
    private List<RoleConfig> roles = new LinkedList<>();

    private RememberMeConfig rememberMe;

    public List<PermissionConfig> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionConfig> permissions) {
        this.permissions = permissions;
    }

    public List<RoleConfig> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleConfig> roles) {
        this.roles = roles;
    }

    public RememberMeConfig getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(RememberMeConfig rememberMe) {
        this.rememberMe = rememberMe;
    }

    /**
     * <h2>权限配置类</h2>
     */
    public static class PermissionConfig {
        /**
         * <p>
         *     父权限的名称.
         * </p>
         *
         * @see Permission#getName() name
         */
        private String pname = null;
        /**
         * @see Permission#getName() name
         */
        private String name;
        /**
         * @see Permission#getPermission() permission
         */
        private String permission;
        /**
         * @see Permission#getDescription() description
         */
        private String description;

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
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

        public @NonNull Permission toPermission() {
            return new Permission(this.name, this.permission, this.description);
        }

        public boolean hasParentPermission() {
            return StringUtils.hasText(this.pname);
        }
    }

    /**
     * <h2>角色配置类</h2>
     */
    public static class RoleConfig {
        /**
         * @see Role#getName() name
         */
        private indi.ly.crush.enums.Role name;
        /**
         * <p>
         *     角色对应的所有权限的名称.
         * </p>
         *
         * @see Permission#getName() name
         */
        private List<String> permissions;

        public indi.ly.crush.enums.Role getName() {
            return name;
        }

        public void setName(indi.ly.crush.enums.Role name) {
            this.name = name;
        }

        public List<String> getPermissions() {
            return permissions;
        }

        public void setPermissions(List<String> permissions) {
            this.permissions = permissions;
        }

        public @NonNull Role toRole() {
            return new Role(this.name.name());
        }
    }

    /**
     * <h2>记住我配置类</h2>
     *
     * @see SimpleCookie
     * @see ShiroConfig#createCookieRememberMeManagerBean(AppProperties)
     */
    public static class RememberMeConfig {
        /**
         * <p>
         *     {@code RememberMe Cookie} 的名称, 默认为 {@code rememberMe}.
         * </p>
         *
         * @see SimpleCookie#setName(String)
         */
        private String cookieName = "rememberMe";
        /**
         * <p>
         *     {@code RememberMe Cookie} 的有效期, 默认为 {@code 30} 天.
         * </p>
         *
         * @see SimpleCookie#setMaxAge(int)
         */
        private int maxAge = 30;
        /**
         * <p>
         *     {@link #maxAge} 属性的单位, 默认为秒.
         * </p>
         */
        private TimeUnit timeUnit = TimeUnit.DAYS;

        public String getCookieName() {
            return cookieName;
        }

        public void setCookieName(String cookieName) {
            if (BaseStringUtil.notHasText(cookieName)) {
                throw new IllegalArgumentException("不是一个合法的 RememberMe Cookie 名称.");
            }
            this.cookieName = cookieName;
        }

        public int getMaxAge() {
            return timeUnit.convertTimeToSeconds(maxAge);
        }

        public void setMaxAge(int maxAge) {
            if (maxAge <= 0) {
                throw new IllegalArgumentException("不是一个合法的 RememberMe Cookie 有效期.");
            }
            this.maxAge = maxAge;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }

    /**
     * <h2>时间单位枚举类</h2>
     *
     * @see RememberMeConfig
     */
    public enum TimeUnit {
        MINUTES, HOURS, DAYS;

        /**
         * <p>
         *     将给定的时间量转换为秒.
         * </p>
         *
         * @param time 待转换为秒的时间量.
         * @return 以秒为单位的等效时间.
         */
        private int convertTimeToSeconds(int time) {
            return switch (this) {
                case DAYS -> time * 24 * 60 * 60;
                case HOURS -> time * 60 * 60;
                case MINUTES -> time * 60;
            };
        }
    }
}
