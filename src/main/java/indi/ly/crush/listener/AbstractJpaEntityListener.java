package indi.ly.crush.listener;

import indi.ly.crush.domain.AbstractJpaExpansionEntity;
import indi.ly.crush.provider.UsernameProvider;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Objects;

/**
 * <h2>{@code JPA} 实体监听器</h2>
 * <p>
 *     用于在实体数据持久化(即插入到数据库)和更新时自动填充用户名. <br /> <br />
 *
 *     注意: 实体数据的插入(持久化)和更新操作应在用户已认证之后执行, 确保数据安全和完整性.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
abstract class AbstractJpaEntityListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJpaEntityListener.class);
    private final UsernameProvider usernameProvider;

    public AbstractJpaEntityListener(@NonNull UsernameProvider usernameProvider) {
        this.usernameProvider = Objects.requireNonNull(usernameProvider, "必须提供一个 indi.ly.crush.provider.UsernameProvider 实现来保证用户名的获取.");
    }

    /**
     * <p>
     *     {@code JPA} 实体被插入(持久化)之前自动调用.
     * </p>
     *
     * @param object {@code JPA} 实体对象.
     */
    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof AbstractJpaExpansionEntity<?> entity) {
            String className = entity.getClass().getName();
            LOGGER.debug("实体 [{}] 插入之前的状态: {}", className, entity);
            String currentUser = this.usernameProvider.getCurrentUsername();
            entity.setCreatedBy(currentUser);
            entity.setLastModifiedBy(currentUser);
            LOGGER.debug("自动填充 createdBy 和 lastModifiedBy 属性完毕, 实体 [{}] 插入之后的状态: {}", className, entity);
        }
    }

    /**
     * <p>
     *     {@code JPA} 实体被更新之前自动调用.
     * </p>
     *
     * @param object {@code JPA} 实体对象.
     */
    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof AbstractJpaExpansionEntity<?> entity) {
            String className = entity.getClass().getName();
            LOGGER.debug("实体 [{}] 更新之前的状态: {}", className, entity);
            String currentUser = this.usernameProvider.getCurrentUsername();
            LOGGER.debug("自动填充 createdBy 和 lastModifiedBy 属性完毕, 实体 [{}] 更新之后的状态: {}", className, entity);
            entity.setLastModifiedBy(currentUser);
        }
    }
}
