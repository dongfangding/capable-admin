package com.ddf.boot.capableadmin.controller.auth;

import com.ddf.boot.capableadmin.application.AuthApplicationService;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.request.auth.AdminLoginRequest;
import com.ddf.boot.capableadmin.model.response.auth.PrettyAdminLoginResponse;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器。
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    /**
     * 管理后台登录。
     *
     * @param request 登录请求
     * @param httpRequest HTTP请求对象
     * @return 登录结果
     */
    @PostMapping("login")
    public ResponseData<PrettyAdminLoginResponse> login(@RequestBody @Valid AdminLoginRequest request,
            HttpServletRequest httpRequest) {
        return ResponseData.success(authApplicationService.login(request, httpRequest));
    }

    /**
     * 当前用户退出登录。
     *
     * @param request HTTP请求对象
     * @return 是否退出成功
     */
    @PostMapping("logout")
    public ResponseData<Boolean> logout(HttpServletRequest request) {
        authApplicationService.logout();
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 查询当前用户权限码列表。
     *
     * @return 权限码集合
     */
    @GetMapping("codes")
    public ResponseData<List<String>> codes() {
        return ResponseData.success(new ArrayList<>(PrettyAdminSecurityUtils.getCurrentUser().getPermissions()));
    }
}
