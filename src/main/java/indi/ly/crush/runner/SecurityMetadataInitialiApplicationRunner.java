package indi.ly.crush.runner;

import indi.ly.crush.config.AppProperties;
import indi.ly.crush.listener.JpaEntityListenerProcessedByShiro;
import indi.ly.crush.model.entity.Permission;
import indi.ly.crush.model.entity.Role;
import indi.ly.crush.provider.ShiroBasedUsernameProvider;
import indi.ly.crush.repository.IPermissionRepository;
import indi.ly.crush.repository.IRoleRepository;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Session;
import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h2>安全元数据初始化应用运行程序</h2>
 * <p>
 *     初始化权限和角色的数据, 确保应用启动时具备基本的安全元数据. <br /> <br />
 *
 *     请注意, {@link Role}、{@link Permission} 等实体被 {@link JpaEntityListenerProcessedByShiro} 监听器所监听,
 *     因此会触发 {@link ShiroBasedUsernameProvider#getCurrentUsername()} 方法间接调用了:
 *     <pre>{@code
 *                  SecurityUtils.getSubject()
 *     }</pre>
 *     这是在 {@code Shiro} 管理之外的线程中调用 {@link SecurityUtils#getSubject()} 方法.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Component
public class SecurityMetadataInitialiApplicationRunner
        implements ApplicationRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityMetadataInitialiApplicationRunner.class);
    private final AppProperties appProperties;
    private final IRoleRepository roleRepositoryImpl;
    private final IPermissionRepository permissionRepositoryImpl;
    @PersistenceContext
    private EntityManager entityManager;

    public SecurityMetadataInitialiApplicationRunner(
            AppProperties appProperties,
            IRoleRepository roleRepositoryImpl,
            IPermissionRepository permissionRepositoryImpl
    ) {
        this.appProperties = appProperties;
        this.roleRepositoryImpl = roleRepositoryImpl;
        this.permissionRepositoryImpl = permissionRepositoryImpl;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            List<Role> roleGroup = this.initializeRolesIfNeeded();
            Map<String, Permission> permissionMap = this.initializePermissionsIfNeeded();
            this.assignPermissionsToRoles(roleGroup, permissionMap);
        } catch (Exception e) {
            LOGGER.error("初始化认证元数据失败.", e);
            // 在出现异常时重新抛出, 会导致应用启动失败, 这取决于具体的应用需求.
            throw e;
        }
    }

    /**
     * <p>
     *     初始化权限数据, 如果数据库中没有权限数据, 则根据配置文件中的权限定义创建权限.
     * </p>
     *
     * @return 权限名称到权限实体的映射(含 ID 值, 但不含 PID 值).
     */
    private Map<String, Permission> initializePermissionsIfNeeded() {
        // 检查数据库是否已存在权限数据.
        if (this.permissionRepositoryImpl.count() > 0) {
            return Collections.emptyMap();
        }

        // 将配置中的权限转换为权限实体列表.
        List<Permission> permissions = this.appProperties.getPermissions()
                                                                        .stream()
                                                                        .map(AppProperties.PermissionConfig :: toPermission)
                                                                        .toList();

        // 保存所有权限到数据库, 并创建一个映射, 用权限名称映射到权限实体, 以便后续建立父子关系.
        Map<String, Permission> permissionMap = this.permissionRepositoryImpl.saveAll(permissions)  // 保存所有权限, 但不立即设置它们的父子关系.
                                                                                    .stream()
                                                                                    .collect(Collectors.toMap(Permission :: getName, Function.identity()));

        LOGGER.info("初始化默认权限完毕.");

        // 建立权限之间的父子关系.
        this.establishParentChildRelationships(permissionMap);

        return permissionMap;
    }

    /**
     * <p>
     *     建立权限之间的父子关系. <br /> <br />
     *
     *     为什么要多此一举使用{@code 权限名称}({@code pname})而不是{@code 数据库 ID}({@code pid})来建立关系的原因如下:
     *     <ol>
     *         <li>
     *             配置文件可读性: <br />
     *             使用人类可读的名称来定义权限之间的关系, 比起使用难以记忆的数字 {@code ID}, 可以大大提高配置文件的可读性和易维护性. <br />
     *             而且, {@code ID} 是数据库生成的数字, 可能难以追踪.
     *         </li>
     *         <li>
     *             避免直接依赖数据库 {@code ID}(初始化时{@code 数据库 ID} 未知): <br />
     *             在应用启动时, 数据库中尚未存在任何权限数据, 因此无法预先知道任何权限的{@code 数据库 ID}. <br />
     *             使用名称({@code pname})作为引用允许我们在不依赖数据库生成的 {@code ID} 的情况下, 预先定义好权限之间的层级关系.
     *         </li>
     *         <li>
     *             灵活性和解耦: <br />
     *             这种设计使得权限数据的初始化逻辑不直接依赖于数据库中的具体 {@code ID} 值(减少了对特定{@code 数据库 ID}的依赖),
     *             使得权限结构更容易在不同环境或数据库间迁移和重用.
     *         </li>
     *     </ol>
     *     通过采用这种设计, 可以在应用的早期阶段灵活地定义和调整权限结构, 而不必担心数据库中的具体 {@code ID} 值, 从而简化了权限管理和应用配置的过程.
     * </p>
     *
     * @param permissionMap 通过权限名称映射到权限实体的映射, 用于查找和设置父权限.
     */
    private void establishParentChildRelationships(Map<String, Permission> permissionMap) {
        // 创建一个列表, 用于收集需要更新的权限实体.
        List<Permission> permissionsToUpdate = new ArrayList<>(permissionMap.size());

        for (AppProperties.PermissionConfig config : this.appProperties.getPermissions()) {
            // 获取当前权限实体.
            Permission currentPermission = permissionMap.get(config.getName());
            // 如果配置中指定了父权限名称.
            if (config.hasParentPermission()) {
                // 从权限映射中查找父权限实体.
                Permission parentPermission = permissionMap.get(config.getPname());
                // 如果找到父权限, 设置当前权限的父权限并保存更新.
                if (parentPermission != null) {
                    currentPermission.setPid(parentPermission.getId());
                    // 将需要更新的权限实体添加到列表中.
                    permissionsToUpdate.add(currentPermission);
                }
            }
        }

        // 批量保存所有需要更新的权限实体, 以减少数据库请求次数.
        if (!permissionsToUpdate.isEmpty()) {
            this.permissionRepositoryImpl.saveAll(permissionsToUpdate);
        }
    }

    /**
     * <p>
     *     初始化角色数据, 如果数据库中没有角色数据, 则根据配置文件中的角色定义创建角色.
     * </p>
     *
     * @return 已保持到数据库的角色组.
     */
    private List<Role> initializeRolesIfNeeded() {
        // 检查数据库是否已存在角色数据.
        if (this.roleRepositoryImpl.count() > 0) {
            return Collections.emptyList();
        }

        // 将配置中的角色转换为角色实体列表.
        List<Role> roles = this.appProperties.getRoles()
                                                    .stream()
                                                    .map(AppProperties.RoleConfig :: toRole)
                                                    .toList();

        List<Role> saveRoles = this.roleRepositoryImpl.saveAll(roles);
        LOGGER.info("初始化默认角色完毕.");

        return saveRoles;
    }

    /**
     * <p>
     *     为每个角色分配相应的权限. <br /> <br />
     *
     *     遍历角色列表, 对于每个角色, 根据角色名称查找其应分配的权限名称列表. <br />
     *     然后, 通过权限映射找到这些权限名称对应的权限实体, 并构建角色 {@code ID} 和权限 {@code ID} 的关联数据. <br />
     *     最后, 如果有关联数据, 使用批量插入操作将这些数据保存到数据库中.
     * </p>
     *
     * @param roles         待分配权限的角色列表.
     * @param permissionMap 权限名称到权限实体的映射, 用于快速查找权限实体.
     */
    private void assignPermissionsToRoles(List<Role> roles, Map<String, Permission> permissionMap) {
        //  准备角色权限关联数据列表.
        List<Object[]> rolePermissions = new ArrayList<>();

        // 为每个角色构建其权限关联.
        roles.forEach(role -> {
            // 根据角色名称查找其对应的权限名称列表.
            List<String> permissionNames = this.appProperties.getRoles()
                                                                    .stream()
                                                                    .filter(rc -> rc.getName().name().equals(role.getName()))
                                                                    .findFirst()
                                                                    .map(AppProperties.RoleConfig :: getPermissions)
                                                                    .orElse(Collections.emptyList());

            // 遍历权限名称, 查找对应的权限实体, 并构建角色 ID 和权限 ID 的对应关系.
            permissionNames
                        .stream()
                        .map(permissionMap :: get)
                        .filter(Objects :: nonNull)   // 确保权限实体存在.
                        .forEach(permission -> rolePermissions.add(new Object[] {role.getId(), permission.getId()}));
        });

        // 如果存在角色权限关联数据, 执行批量插入操作.
        if (!rolePermissions.isEmpty()) {
            this.batchInsertRolePermissions(rolePermissions);
        }
    }

    /**
     * <p>
     *     使用批量操作插入角色和权限的关联数据.
     * </p>
     *
     * @param rolePermissions 角色 {@code ID} 和权限 {@code ID} 的对应关系列表.
     */
    private void batchInsertRolePermissions(List<Object[]> rolePermissions) {
        @Language("MySQL")
        String sql = "INSERT INTO t_role_permissions(role_id, permissions_id) VALUES (?, ?)";

        /*
            确保 Session 关闭的责任在于管理 EntityManager 的代码或框架(比如Spring Data JPA), 而不是直接操作 Session 的代码本身.
            当你在事务性环境中工作, 通常是事务管理器负责在事务结束时关闭 EntityManager(以及它内部的 Session).

            总之, 在使用 EntityManager.unwrap(Session.class) 获取 Session 进行操作的情况下, 你不需要(也不应该)手动关闭这个 Session.
            这是因为这会干扰 EntityManager 对会话生命周期的管理, 可能导致未预期的行为或错误.
         */

        this.entityManager.unwrap(Session.class).doWork(connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                for (Object[] rp : rolePermissions) {
                    ps.setLong(1, (Long) rp[0]);
                    ps.setLong(2, (Long) rp[1]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        });
    }
}
