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
     *     使用原生 {@code SQL} 根据给定的用户名(<em>username</em>)查找与之关联的所有角色名称.
     * </p>
     *
     * @param username 用户名, 用于查找与之关联的角色.
     * @return 一个包含角色名称的集合. 如果用户不存在或没有关联的角色, 返回空集合.
     */
    @Query(value = " SELECT r.name FROM t_role r JOIN t_user_role ur ON r.id = ur.role_id JOIN t_user u ON ur.user_id = u.id WHERE u.username = :username ", nativeQuery = true)
    Set<String> findRolesByUsername(@Param("username") String username);
}