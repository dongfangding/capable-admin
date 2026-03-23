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
 * 用户缓存仓库。
 *
 * 这里负责用户登录详情、在线用户信息等 Redis 读写。
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
     * 设置用户登录详情。
     *
     * 额外打印 userId 的运行时类型和最终 key，方便排查是否有数组被误传入 key 生成逻辑。
     *
     * @param details 用户详情
     */
    public void setUserLoginDetails(PrettyAdminUserDetails details) {
        Long userId = details.getUserId();
        String userDetailsKey = PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(userId.toString());
        redisCommandHelper.set(userDetailsKey, JsonUtil.toJson(details));
    }

    /**
     * 获取用户登录详情。
     *
     * @param userId 用户ID
     * @return 用户详情
     */
    public PrettyAdminUserDetails getUserLoginDetails(Long userId) {
        final String key = PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(userId.toString());
        final String string = redisCommandHelper.get(key);
        return StringUtils.isNotBlank(string) ? JsonUtil.toBean(string, PrettyAdminUserDetails.class) : null;
    }

    /**
     * 设置在线用户信息。
     *
     * @param token   登录 token
     * @param sysUser 用户实体
     */
    public void setOnlineUser(String token, SysUser sysUser) {
        // 保存在线用户信息到 Redis
        String onlineUserKey = PrettyAdminRedisKeyEnum.ONLINE_USER.getKey(token);

        // 构建在线用户信息，当前先保存简化版
        String userInfo = String.format("{\"userId\":%d,\"username\":\"%s\",\"loginTime\":%d}",
                sysUser.getUserId(), sysUser.getUsername(), System.currentTimeMillis());

        redisCommandHelper.setEx(onlineUserKey, userInfo,
                PrettyAdminRedisKeyEnum.ONLINE_USER.getTtl().getSeconds());
    }
}
