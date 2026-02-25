package com.ddf.boot.capableadmin.application;

import com.ddf.boot.common.api.model.authentication.AuthenticateToken;
import com.ddf.boot.common.api.model.authentication.UserClaim;
import com.ddf.boot.common.authentication.util.UserContextUtil;
import com.ddf.boot.common.core.authentication.TokenUtil;
import com.ddf.boot.common.core.encode.BCryptPasswordEncoder;
import com.ddf.boot.common.core.util.PreconditionUtil;
import com.ddf.boot.common.mvc.util.WebUtil;
import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.request.auth.AdminLoginRequest;
import com.ddf.boot.capableadmin.model.response.auth.PrettyAdminLoginResponse;
import com.ddf.boot.capableadmin.service.PrettyAdminCacheManager;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 认证应用服务
 * </p>
 * 核心改进:登录时强制刷新所有缓存,解决缓存不更新问题
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 17:50
 */
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
@Service
public class AuthApplicationService {

    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrettyAdminCacheManager cacheManager;
    private final RedisCommandHelper redisCommandHelper;

    /**
     * 用户登录
     * 核心改进:登录时强制刷新所有缓存
     *
     * @param request     登录请求
     * @param httpRequest HTTP请求
     * @return 登录响应
     */
    @Transactional(rollbackFor = Exception.class)
    public PrettyAdminLoginResponse login(AdminLoginRequest request, HttpServletRequest httpRequest) {
        // 1. 验证用户名密码
        final SysUser sysUser = validateCredentials(request);

        // 2. 【关键】清理该用户的所有旧缓存
        log.info("用户登录,清理旧缓存, userId: {}, username: {}", sysUser.getUserId(), sysUser.getUsername());
        cacheManager.cleanUserAllCache(sysUser.getUserId());

        // 3. 生成新Token
        final UserClaim claim = new UserClaim();
        claim.setUserId(String.valueOf(sysUser.getUserId()));
        claim.setUsername(sysUser.getUsername());
        claim.setCredit(WebUtil.getUserAgent());
        final AuthenticateToken authenticateToken = TokenUtil.createToken(claim);
        final String token = authenticateToken.getToken();

        // 4. 保存在线用户信息
        saveOnlineUser(sysUser, token, httpRequest);

        // 5. 返回登录信息
        final PrettyAdminLoginResponse response = new PrettyAdminLoginResponse();
        response.setAccessToken(token);

        log.info("用户登录成功, userId: {}, username: {}", sysUser.getUserId(), sysUser.getUsername());
        return response;
    }

    /**
     * 用户登出
     * 核心改进:清理所有相关缓存
     *
     */
    public void logout() {
        try {
            // 1. 从Token中提取用户ID
            final UserClaim userClaim = UserContextUtil.getUserClaim();
            final Long userId = Long.parseLong(userClaim.getUserId());

            // 2. 删除在线用户记录
            cacheManager.cleanOnlineUserCache(UserContextUtil.getRequestContext().getToken());

            // 3. 【关键】清理用户所有缓存
            log.info("用户登出,清理缓存, userId: {}", userId);
            cacheManager.cleanUserAllCache(userId);

            log.info("用户登出成功, userId: {}", userId);
        } catch (Exception e) {
            log.error("用户登出失败", e);
        }
    }

    /**
     * 验证用户凭证
     *
     * @param request 登录请求
     * @return 用户信息
     */
    private SysUser validateCredentials(AdminLoginRequest request) {
        final String username = request.getUsername();
        final SysUser sysUser = sysUserMapper.selectByUsername(username);

        PreconditionUtil.checkArgument(Objects.nonNull(sysUser),
                PrettyAdminExceptionCode.SYS_USER_NOT_EXISTS);

        final boolean isMatch = bCryptPasswordEncoder.matches(
                request.getPassword(), sysUser.getPassword());

        PreconditionUtil.checkArgument(isMatch,
                PrettyAdminExceptionCode.SYS_PASSWORD_NOT_MATCH);

        return sysUser;
    }

    /**
     * 保存在线用户信息
     *
     * @param sysUser 用户信息
     * @param token   Token
     * @param request HTTP请求
     */
    private void saveOnlineUser(SysUser sysUser, String token, HttpServletRequest request) {
        // 保存在线用户信息到Redis
        String onlineUserKey = PrettyAdminRedisKeyEnum.ONLINE_USER.getKey(token);

        // 构建在线用户信息(简化版,可根据需要扩展)
        String userInfo = String.format("{\"userId\":%d,\"username\":\"%s\",\"loginTime\":%d}",
                sysUser.getUserId(), sysUser.getUsername(), System.currentTimeMillis());

        redisCommandHelper.setEx(onlineUserKey, userInfo,
                PrettyAdminRedisKeyEnum.ONLINE_USER.getTtl().getSeconds());

        log.debug("保存在线用户信息, userId: {}, token: {}", sysUser.getUserId(), token);
    }
}
