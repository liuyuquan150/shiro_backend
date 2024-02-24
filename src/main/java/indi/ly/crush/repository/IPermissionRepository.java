package indi.ly.crush.repository;

import indi.ly.crush.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * <h2>权限存储库</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface IPermissionRepository
		extends JpaRepository<Permission, Long> {

}