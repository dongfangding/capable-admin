package com.ddf.boot.capableadmin.infra.repository;

import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.common.api.util.JsonUtil;
import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/03/20 18:42
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Repository
public class SysUserRepository {

	private final RedisCommandHelper redisCommandHelper;


	/**
	 * 设置用户登录详情
	 *
	 * @param details
	 */
	public void setUserLoginDetails(PrettyAdminUserDetails details) {
		redisCommandHelper.set(
				PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(details
						.getUserId()
						.toString()), JsonUtil.toJson(details)
		);
	}

	/**
	 * 获取用户登录详情
	 *
	 * @param userId
	 * @return
	 */
	public PrettyAdminUserDetails getUserLoginDetails(Long userId) {
		final String string = redisCommandHelper.get(PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(userId.toString()));
		return StringUtils.isNotBlank(string) ? JsonUtil.toBean(string, PrettyAdminUserDetails.class) : null;
	}

	public void setOnlineUser(String token, SysUser sysUser) {
		// 保存在线用户信息到Redis
		String onlineUserKey = PrettyAdminRedisKeyEnum.ONLINE_USER.getKey(token);

		// 构建在线用户信息(简化版,可根据需要扩展)
		String userInfo = String.format("{\"userId\":%d,\"username\":\"%s\",\"loginTime\":%d}",
				sysUser.getUserId(), sysUser.getUsername(), System.currentTimeMillis());

		redisCommandHelper.setEx(onlineUserKey, userInfo,
				PrettyAdminRedisKeyEnum.ONLINE_USER.getTtl().getSeconds());
	}

}
