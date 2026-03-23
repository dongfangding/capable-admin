package com.ddf.boot.capableadmin.infra.util;

import cn.dev33.satoken.stp.StpUtil;
import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.infra.repository.SysUserRepository;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.service.PrettyAdminUserDetailsService;
import com.ddf.boot.common.api.exception.UnauthorizedException;
import com.ddf.boot.common.api.util.JsonUtil;
import com.ddf.boot.common.core.helper.SpringContextHolder;
import javax.swing.Spring;

/**
 * 当前登录用户安全工具类。
 */
public class PrettyAdminSecurityUtils {

	private static final SysUserRepository SYS_USER_REPOSITORY;
	private static final PrettyAdminUserDetailsService PRETTY_ADMIN_USER_DETAILS_SERVICE;

	static {
		SYS_USER_REPOSITORY = SpringContextHolder.getBean(SysUserRepository.class);
		PRETTY_ADMIN_USER_DETAILS_SERVICE = SpringContextHolder.getBean(PrettyAdminUserDetailsService.class);
	}

    /**
     * 获取当前登录用户ID。
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
     * 获取当前登录用户详情。
     *
     * @return 当前用户详情
     */
    public static PrettyAdminUserDetails getCurrentUser() {
        StpUtil.checkLogin();
        long userId = StpUtil.getLoginIdAsLong();
		return SYS_USER_REPOSITORY.getUserLoginDetails(userId);
//        return PRETTY_ADMIN_USER_DETAILS_SERVICE.loadUserById(userId);
    }


    /**
     * 获取当前登录用户名。
     *
     * @return 用户名
     */
    public static String getCurrentUsername() {
        PrettyAdminUserDetails user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }
}
