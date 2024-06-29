package indi.ly.crush.controller;

import indi.ly.crush.model.vo.UserVO;
import indi.ly.crush.model.entity.User;
import indi.ly.crush.model.from.UserCredentials;
import indi.ly.crush.model.from.UserRegistration;
import indi.ly.crush.response.ResponseResult;
import indi.ly.crush.service.IAccountService;
import indi.ly.crush.util.support.BaseSpringBeanUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h2>账户控制器</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
@RestController
@RequestMapping(value = "/api/")
public class AccountController {

    private final IAccountService userServiceImpl;

    public AccountController(IAccountService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping(value = "/v1/register")
    public ResponseResult<?> goToRegister(@RequestBody UserRegistration userRegistration) {
       this.userServiceImpl.registerUserAndAssignDefaultRole(userRegistration);
        return ResponseResult.ok("注册成功");
    }

    @PostMapping(value = "/v1/login")
    public ResponseResult<UserVO> goToLogin(@RequestBody UserCredentials userCredentials) {
        User user = this.userServiceImpl.login(userCredentials);
        UserVO userVO = BaseSpringBeanUtil.shallowCopyObject(user, new UserVO(), null, null);
        return ResponseResult.ok(userVO).message("登录成功");
    }

    @PostMapping(value = "/v1/logout")
    public ResponseResult<?> goToLogout() {
        SecurityUtils.getSubject().logout();
        return ResponseResult.ok("登出成功");
    }
}
