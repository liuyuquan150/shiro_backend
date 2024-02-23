package indi.ly.crush.runner;

import indi.ly.crush.listener.JpaEntityListenerProcessedByShiro;
import indi.ly.crush.model.entity.Role;
import indi.ly.crush.provider.ShiroBasedUsernameProvider;
import indi.ly.crush.repository.IRoleRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h2>角色数据初始化器应用运行程序</h2>
 * <p>
 *     请注意, {@link Role} 实体被 {@link JpaEntityListenerProcessedByShiro} 监听器所监听,
 *     因此会触发 {@link ShiroBasedUsernameProvider#getCurrentUsername()} 方法间接调用了:
 *     <pre>{@code
 *                  SecurityUtils.getSubject()
 *     }</pre>
 *     这是在 {@code Shiro} 管理之外的线程中调用 {@link SecurityUtils#getSubject()} 方法.
 * </p>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@Component
public class RoleDataInitializerApplicationRunner
        implements ApplicationRunner {

    private final IRoleRepository roleRepositoryImpl;
    private final List<String> roleNames;

    public RoleDataInitializerApplicationRunner(
            IRoleRepository roleRepositoryImpl,
            @Value("${app.roles:#{null}}") List<String> roleNames
    ) {
        this.roleRepositoryImpl = roleRepositoryImpl;
        this.roleNames = roleNames;
    }

    @Override
    public void run(ApplicationArguments args) {
        this.initializeRolesIfNeeded();
    }

    /**
     * <p>
     *     如果需要, 则初始化角色.
     * </p>
     */
    private void initializeRolesIfNeeded() {
        if (this.roleNames != null && this.roleRepositoryImpl.count() == 0) {
            List<Role> roles = this.roleNames
                                            .stream()
                                            .map(Role :: new)
                                            .toList();
            this.roleRepositoryImpl.saveAll(roles);
            System.out.println("Initialized default roles.");
        }
    }
}
