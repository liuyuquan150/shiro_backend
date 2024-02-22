package indi.ly.crush.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <h2>{@code JPA} 基础实体类</h2>
 * <p>
 *     这是一个 Java 实体类的基础抽象类, 使用 JPA(<em>Java Persistence API</em>)注解进行了标注. <br />
 *     这个抽象类定义了一些通用属性和配置, 可以被其它实体类继承, 以避免代码重复.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @param <PK> 主键的数据类型, 必须是一个 {@link Serializable}.
 * @formatter:off
 */
@MappedSuperclass
public abstract class AbstractJpaEntity<PK extends Serializable>
        implements Serializable {
    /**
     * <p>
     *     主键.
     * </p>
     */
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private PK id;
    /**
     * <p>
     *     创建时间.
     * </p>
     */
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createTime;
    /**
     * <p>
     *     最后修改时间.
     * </p>
     */
    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime lastModifiedTime;

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
