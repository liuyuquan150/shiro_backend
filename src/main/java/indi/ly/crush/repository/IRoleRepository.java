package indi.ly.crush.repository;

import indi.ly.crush.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * <h2>角色存储库</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IRoleRepository
		extends JpaRepository<Role, Long> {
	/**
	 * <p>
	 *     将指定的角色分配给用户. <br /> <br />
	 *
	 *     使用原生 {@code SQL} 插入命令来在 {@code t_user_role} 表中创建一个新的用户角色关系. <br />
	 *     这个表链接用户和角色, 每一行表示一个用户被分配了一个特定的角色.
	 * </p>
	 *
	 * @param userId 用户的唯一标识符, 指代具体的用户.
	 * @param roleId 角色的唯一标识符, 指代被分配给用户的特定角色.
	 * @apiNote
	 * 			请注意, 提供的 {@code userId} 和 {@code roleId} 所表示的用户跟角色在对应的表中必须存在,
	 * 			如果 {@code userId} 或 {@code roleId} 所表示的用户或角色不存在,
	 * 			在插入过程中可能会抛出 {@code SQL} 异常或产生冗余数据(取决数据库表有没有设置外键).
	 */
	@Modifying
	@Query(value = " INSERT INTO t_user_role (user_id, role_id) VALUES (?1, ?2) ", nativeQuery = true)
	void assignRoleToUser(Long userId, Long roleId);
}