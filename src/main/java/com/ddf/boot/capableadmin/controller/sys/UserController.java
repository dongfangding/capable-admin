package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.request.sys.UserChangePasswordRequest;
import com.ddf.boot.capableadmin.model.request.sys.UserProfileUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.UserProfileResponse;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户管理
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/07 15:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final SysUserApplicationService sysUserApplicationService;

	/**
	 * 获取当前登录用户信息
	 *
	 * @return
	 */
	@GetMapping("info")
	public ResponseData<PrettyAdminUserDetails> info() {
		return ResponseData.success(PrettyAdminSecurityUtils.getCurrentUser());
	}

    /**
     * 获取当前登录用户可编辑的个人资料。
     *
     * @return 个人资料信息
     */
    @GetMapping("profile")
    public ResponseData<UserProfileResponse> profile() {
        return ResponseData.success(sysUserApplicationService.getCurrentUserProfile(PrettyAdminSecurityUtils.getCurrentUser().getUserId()));
    }

    /**
     * 修改当前登录用户个人资料。
     *
     * @param request 个人资料修改请求
     * @return 是否修改成功
     */
    @PostMapping("profile")
    @AdminAuditLog(module = "个人中心", action = "修改个人资料")
    public ResponseData<Boolean> updateProfile(@RequestBody @Valid UserProfileUpdateRequest request) {
        sysUserApplicationService.updateCurrentUserProfile(PrettyAdminSecurityUtils.getCurrentUserId(), request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 修改当前登录用户密码。
     *
     * @param request 修改密码请求
     * @return 是否修改成功
     */
    @PostMapping("change-password")
    @AdminAuditLog(module = "个人中心", action = "修改密码")
    public ResponseData<Boolean> changePassword(@RequestBody @Valid UserChangePasswordRequest request) {
        sysUserApplicationService.changeCurrentUserPassword(PrettyAdminSecurityUtils.getCurrentUserId(), request);
        return ResponseData.success(Boolean.TRUE);
    }
}
