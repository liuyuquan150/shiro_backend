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
 *     用于自动填充插入(<em>持久化</em>)和更新实体时的用户名. <br />
 *     请注意, 实体的插入(<em>持久化</em>)和更新操作应该在已经认证用户之后进行.
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
        this.usernameProvider = Objects.requireNonNull(usernameProvider, "UsernameProvider cannot be null.");
    }

    /**
     * <p>
     *     {@code JPA} 实体被插入(<em>持久化</em>)之前自动调用.
     * </p>
     *
     * @param object {@code JPA} 实体对象.
     */
    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof AbstractJpaExpansionEntity<?> entity) {
            String currentUser = this.usernameProvider.getCurrentUsername();
            LOGGER.debug("Setting createdBy and lastModifiedBy for [{}] to \"{}\".", entity.getClass().getSimpleName(), currentUser);
            entity.setCreatedBy(currentUser);
            entity.setLastModifiedBy(currentUser);
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
            String currentUser = this.usernameProvider.getCurrentUsername();
            LOGGER.debug("Updating lastModifiedBy for [{}] to \"{}\".", entity.getClass().getSimpleName(), currentUser);
            entity.setLastModifiedBy(currentUser);
        }
    }
}
