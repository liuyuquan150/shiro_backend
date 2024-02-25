package indi.ly.crush.repository;

import indi.ly.crush.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * <h2>用户存储库</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IUserRepository
        extends JpaRepository<User, Long> {
    /**
     * <p>
     *     使用 {@code JPQL} 根据给定的用户名(<em>username</em>)查找与之关联的用户.
     * </p>
     *
     * @param username 用户名, 用于查找与之关联的用户.
     * @return 一个包含用户名、密码和盐值的 {@link User} 实例. 如果没有找到匹配的用户, 返回 {@code null}.
     */
    @Query(value = "SELECT new User(u.username, u.password, u.salt) FROM User u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username);
    /**
     * <p>
     *     使用原生 {@code SQL} 根据给定的用户名(<em>username</em>)查找与之关联的所有角色名称.
     * </p>
     *
     * @param username 用户名, 用于查找与之关联的角色.
     * @return 一个包含角色名称的集合. 如果用户不存在或没有关联的角色, 返回空集合.
     */
    @Query(
            value = """
                    SELECT r.name FROM t_role r
                    JOIN t_user_role ur ON r.id = ur.role_id
                    JOIN t_user u ON ur.user_id = u.id
                    WHERE u.username = :username
                    """,
            nativeQuery = true
    )
    Set<String> findRolesByUsername(@Param("username") String username);

    /**
     * <p>
     *     使用原生 {@code SQL} 根据给定的用户名(<em>username</em>)查找与之关联的所有直接权限的资源标识符.
     * </p>
     *
     * @param username 用户名, 用于查找与之关联的直接权限.
     * @return 一个包含权限资源标识符的集合. 如果用户不存在或没有关联的直接权限, 返回空集合.
     */
    @Query(
            value = """
                    SELECT p.shiro_permission FROM t_permission p
                    JOIN t_user_permission up ON p.id = up.permission_id
                    JOIN t_user u ON up.user_id = u.id
                    WHERE u.username = :username
                    """,
            nativeQuery = true
    )
    Set<String> findPermissionsByUsername(@Param("username") String username);

    /**
     * <p>
     *     使用原生 {@code SQL} 查询并返回指定用户名(<em>username</em>)的用户通过其角色获得的所有角色权限(间接权限)的资源标识符. <br /> <br />
     *
     *     首先确定用户所拥有的角色, 然后找出这些角色所关联的所有权限.
     * </p>
     *
     * @param username 用户名, 用于查找与之关联的角色权限(间接权限).
     * @return 一个包含权限资源标识符的集合. 这些权限是用户通过其角色继承获得的, 如果用户没有通过角色获得任何权限, 或者用户不存在，则返回一个空集合.
     */
    @Query(
            value = """
                   SELECT p.shiro_permission FROM t_permission p
                   JOIN t_role_permissions rp ON p.id = rp.permissions_id
                   JOIN t_role r ON rp.role_id = r.id
                   JOIN t_user_role ur ON r.id = ur.role_id
                   JOIN t_user u ON ur.user_id = u.id
                   WHERE u.username = :username
                   """,
            nativeQuery = true
    )
    Set<String> findRolePermissionsByUsername(@Param("username") String username);
}