package indi.ly.crush.domain;

import indi.ly.crush.listener.JpaEntityListenerProcessedByShiro;
import org.springframework.data.jpa.domain.AbstractAuditable;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * <h2>{@code JPA} 基础实体类</h2>
 * <p>
 *     在 {@link AbstractJpaEntity} 的基础拓展了创建者和最后修改者.
 * </p>
 *
 * @since 1.0
 * @see AbstractAuditable
 * @author 云上的云
 * @param <PK> 主键的数据类型, 必须是一个 {@link Serializable}.
 * @formatter:off
 */
@MappedSuperclass
@EntityListeners(value = JpaEntityListenerProcessedByShiro.class)
public abstract class AbstractJpaExpansionEntity<PK extends Serializable>
        extends AbstractJpaEntity<PK> {
    /**
     * <p>
     *     创建者.
     * </p>
     */
    @Column(nullable = false, updatable = false, length = 128)
    private String createdBy;
    /**
     * <p>
     *     最后修改者.
     * </p>
     */
    @Column(nullable = false, length = 128)
    private String lastModifiedBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
