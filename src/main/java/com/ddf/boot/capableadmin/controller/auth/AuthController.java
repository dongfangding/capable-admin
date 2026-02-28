package com.ddf.boot.capableadmin.controller.auth;

import com.ddf.boot.capableadmin.application.AuthApplicationService;
import com.ddf.boot.capableadmin.model.request.auth.AdminLoginRequest;
import com.ddf.boot.capableadmin.model.response.auth.PrettyAdminLoginResponse;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/09 16:15
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService authApplicationService;

    /**
     * 登录
     *
     * @param request
     * @param httpRequest
     * @return
     */
    @PostMapping("login")
    public ResponseData<PrettyAdminLoginResponse> login(@RequestBody @Valid AdminLoginRequest request,
            HttpServletRequest httpRequest) {
        return ResponseData.success(authApplicationService.login(request, httpRequest));
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("logout")
    public ResponseData<Boolean> logout(HttpServletRequest request) {
        authApplicationService.logout();
        return ResponseData.success(Boolean.TRUE);
    }


	/**
	 * fixme 前端现在需要这个， 还没搞清楚是做按钮权限还是啥的，先把接口定义出来防止报错
	 *
	 * @return
	 */
	@GetMapping("codes")
	public ResponseData<List<String>> codes() {
		return ResponseData.success(List.of("AC_100010", "AC_100020", "AC_100030"));
	}
}
