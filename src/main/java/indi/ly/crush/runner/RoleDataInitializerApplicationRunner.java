package indi.ly.crush.runner;

import indi.ly.crush.enums.Role;
import indi.ly.crush.listener.JpaEntityListenerProcessedByShiro;
import indi.ly.crush.provider.ShiroBasedUsernameProvider;
import indi.ly.crush.repository.IRoleRepository;
import org.apache.shiro.SecurityUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <h2>角色数据初始化器应用运行程序</h2>
 * <p>
 *     请注意, {@link indi.ly.crush.model.entity.Role} 实体被 {@link JpaEntityListenerProcessedByShiro} 监听器所监听,
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

    public RoleDataInitializerApplicationRunner(IRoleRepository roleRepositoryImpl) {
        this.roleRepositoryImpl = roleRepositoryImpl;
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
        if (this.roleRepositoryImpl.count() == 0) {
            List<indi.ly.crush.model.entity.Role> roles = Arrays
                                                                .stream(Role.values())
                                                                .map(Enum :: name)
                                                                .map(indi.ly.crush.model.entity.Role :: new)
                                                                .toList();
            this.roleRepositoryImpl.saveAll(roles);
            System.out.println("Initialized default roles.");
        }
    }
}
