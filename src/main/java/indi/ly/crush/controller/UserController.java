package indi.ly.crush.controller;

import indi.ly.crush.constants.RoleNameConstants;
import indi.ly.crush.model.dto.UserDTO;
import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import indi.ly.crush.model.from.UserRegistration;
import indi.ly.crush.response.ResponseResult;
import indi.ly.crush.service.IUserService;
import indi.ly.crush.util.support.BaseSpringBeanUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

/**
 * <h2>登录控制器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@RestController
@RequestMapping(value = "/api/")
public class UserController {

    private final IUserService userServiceImpl;

    public UserController(IUserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping(value = "/v1/register")
    public ResponseResult<?> goToRegister(@RequestBody UserRegistration userRegistration) {
       this.userServiceImpl.registerUserAndAssignDefaultRole(userRegistration);
        return ResponseResult.ok("注册成功");
    }

    @PostMapping(value = "/v1/login")
    public ResponseResult<UserDTO> goToLogin(@RequestBody UserCredentials userCredentials) {
        User user = this.userServiceImpl.login(userCredentials);
        UserDTO userDTO = BaseSpringBeanUtil.shallowCopyObject(user, new UserDTO(), null, null);
        return ResponseResult.ok(userDTO).message("登录成功");
    }

    @PostMapping(value = "/v1/logout")
    public ResponseResult<?> goToLogout() {
        SecurityUtils.getSubject().logout();
        return ResponseResult.ok("登出成功");
    }

    @RequiresRoles(value = RoleNameConstants.SYSTEM_ADMINISTRATOR)
    @RequiresPermissions(value = {"user:*", "user:delete"}, logical = Logical.OR)
    @PostMapping(value = "/v1/del/user")
    public ResponseResult<?> goToDeleteUser(@RequestParam Long userId) {
        return ResponseResult.ok("编号为 [%d] 的用户删除成功".formatted(userId));
    }
}
