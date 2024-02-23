package indi.ly.crush.model.entity;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serial;

/**
 * <h2>角色</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Entity
@Table(name = "t_role", schema = "shiro_backend")
public class Role
		extends AbstractJpaExpansionEntity<Long> {
	@Serial
	private static final long serialVersionUID = 362498820763181265L;
	/**
	 * <p>
	 *     角色名.
	 * </p>
	 */
	@Column(nullable = false, length = 128)
	private String name;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}