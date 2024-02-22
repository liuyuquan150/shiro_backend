package indi.ly.crush.repository;

import indi.ly.crush.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h2>角色存储库</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IRoleRepository
		extends JpaRepository<Role, String> {
}