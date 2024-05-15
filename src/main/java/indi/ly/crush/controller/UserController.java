package indi.ly.crush.controller;

import indi.ly.crush.constants.RoleNameConstants;
import indi.ly.crush.response.ResponseResult;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h2>用户控制器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@RestController
@RequestMapping(value = "/api/v1/user/")
public class UserController {

    /*
        在没有缓存的前提下, Shiro 每次权限检查都会触发新的授权信息获取过程(AuthorizingRealm#doGetAuthorizationInfo 方法会被调用).

        @RequiresRoles(value = RoleNameConstants.SYSTEM_ADMINISTRATOR)
            - 这个注解要求调用该方法的用户必须拥有 SYSTEM_ADMINISTRATOR 角色.
            - Shiro 会在方法调用前检查当前用户是否拥有这个角色.
            - 这会触发一次 doGetAuthorizationInfo 调用, 以获取用户的角色信息.

        @RequiresPermissions(value = {"user:*", "user:delete"}, logical = Logical.OR)
            - 这个注解要求调用该方法的用户必须拥有 user:* 或 user:delete 权限中的任意一个.
            - Shiro 会在方法调用前检查当前用户是否拥有这些权限.
            - 因为使用了 Logical.OR, Shiro 需要分别检查每一个权限,
              这通常会导致两次 doGetAuthorizationInfo 调用(1 次检查 user:*, 1 次检查 user:delete), 每次检查会获取用户的权限信息.

        由于没有缓存, 每次检查权限和角色都会重新调用 doGetAuthorizationInfo 方法, 从而导致总共调用 3 次.
    */

    @RequiresRoles(value = RoleNameConstants.SYSTEM_ADMINISTRATOR)
    @RequiresPermissions(value = {"user:*", "user:delete"}, logical = Logical.OR)
    @PostMapping(value = "del")
    public ResponseResult<?> goToDeleteUser(@RequestParam Long userId) {
        return ResponseResult.ok("编号为 [%d] 的用户删除成功".formatted(userId));
    }
}
