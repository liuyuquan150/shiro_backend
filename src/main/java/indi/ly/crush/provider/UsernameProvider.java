package indi.ly.crush.provider;

import lombok.NonNull;

/**
 * <h2>用户名提供者</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public interface UsernameProvider {
    /**
     * <p>
     *     获取当前用户名.
     * </p>
     *
     * @return 当前用户名.
     */
    @NonNull String getCurrentUsername();
}
