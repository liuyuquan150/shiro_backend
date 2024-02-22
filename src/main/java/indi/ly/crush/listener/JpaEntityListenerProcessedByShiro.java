package indi.ly.crush.listener;

import indi.ly.crush.provider.ShiroBasedUsernameProvider;

/**
 * <h2>由 {@code Shiro} 处理的 {@code JPA} 实体监听器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public class JpaEntityListenerProcessedByShiro
        extends AbstractJpaEntityListener {
    public JpaEntityListenerProcessedByShiro() {
        super(new ShiroBasedUsernameProvider());
    }
}