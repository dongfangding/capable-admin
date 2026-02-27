package com.ddf.boot.capableadmin.infra.util;

import cn.dev33.satoken.stp.StpUtil;
import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.common.api.exception.UnauthorizedException;
import com.ddf.boot.common.api.util.JsonUtil;

/**
 * <p>
 * 安全工具类
 * </p>
 * 提供当前用户信息访问
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 14:45
 */
public class PrettyAdminSecurityUtils {

	/**
	 * 获取当前登录用户ID
	 *
	 * @return 用户ID
	 */
	public static Long getCurrentUserId() {
		PrettyAdminUserDetails user = getCurrentUser();
		if (user == null) {
			throw new UnauthorizedException("用户未登录");
		}
		return user.getUserId();
	}

	/**
	 * 获取当前用户详情
	 *
	 * @return 用户详情
	 */
	public static PrettyAdminUserDetails getCurrentUser() {
		StpUtil.checkLogin();
		final long userId = StpUtil.getLoginIdAsLong();
		final String detailJson = StpUtil
				.getSession(true)
				.get(PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(Long.toString(userId)))
				.toString();
		return JsonUtil.toBean(detailJson, PrettyAdminUserDetails.class);
	}

	/**
	 * 获取当前用户名
	 *
	 * @return 用户名
	 */
	public static String getCurrentUsername() {
		PrettyAdminUserDetails user = getCurrentUser();
		return user != null ? user.getUsername() : null;
	}
}
