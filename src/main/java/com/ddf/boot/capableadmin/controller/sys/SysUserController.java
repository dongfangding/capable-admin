package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
public class SysUserController {

	/**
	 * 获取当前登录用户信息
	 *
	 * @return
	 */
	@GetMapping("info")
	public ResponseData<PrettyAdminUserDetails> info() {
		return ResponseData.success(PrettyAdminSecurityUtils.getCurrentUser());
	}
}
