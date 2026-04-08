package com.ddf.boot.capableadmin.application;

import cn.dev33.satoken.stp.StpUtil;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.infra.repository.SysUserRepository;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.request.auth.AdminLoginRequest;
import com.ddf.boot.capableadmin.model.response.auth.PrettyAdminLoginResponse;
import com.ddf.boot.capableadmin.service.PrettyAdminCacheManager;
import com.ddf.boot.capableadmin.service.PrettyAdminUserDetailsService;
import com.ddf.boot.common.api.model.captcha.CaptchaType;
import com.ddf.boot.common.api.model.captcha.request.CaptchaCheckRequest;
import com.ddf.boot.common.core.encode.BCryptPasswordEncoder;
import com.ddf.boot.common.core.util.PreconditionUtil;
import com.ddf.common.captcha.helper.CaptchaHelper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2025/01/03 17:50
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthApplicationService {

    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrettyAdminCacheManager cacheManager;
	private final PrettyAdminUserDetailsService prettyAdminUserDetailsService;
	private final SysUserRepository sysUserRepository;
	private final CaptchaHelper captchaHelper;

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
        // 校验验证码
        validateCaptcha(request);

        final SysUser sysUser = validateCredentials(request);

		final Long userId = sysUser.getUserId();
		StpUtil.login(userId);

		final PrettyAdminUserDetails details = prettyAdminUserDetailsService.loadUserById(userId);
		sysUserRepository.setUserLoginDetails(details);

		// 返回登录信息
        final PrettyAdminLoginResponse response = new PrettyAdminLoginResponse();
        response.setAccessToken(StpUtil.getTokenValue());
		response.setDetails(details);
        log.info("用户登录成功, userId: {}, username: {}", userId, sysUser.getUsername());
        return response;
    }

    /**
     * 用户登出
     * 核心改进:清理所有相关缓存
     *
     */
    public void logout() {
        try {
			final long userId = StpUtil.getLoginIdAsLong();
			StpUtil.logout();
			// 3. 【关键】清理用户所有缓存
            log.info("用户登出,清理缓存, userId: {}", userId);
            cacheManager.cleanUserAllCache(userId);

            log.info("用户登出成功, userId: {}", userId);
        } catch (Exception e) {
            log.error("用户登出失败", e);
        }
    }

    /**
     * 校验验证码（二次验证）
     * 使用 /captcha/check 接口返回的 captchaVerification 进行二次校验
     *
     * @param request 登录请求
     */
    private void validateCaptcha(AdminLoginRequest request) {
        final CaptchaCheckRequest captchaRequest = CaptchaCheckRequest.builder()
                .uuid(request.getUuid())
                .verifyCode(request.getCode())
                .captchaType(CaptchaType.CLICK_WORDS)
                .verification(true)
                .captchaVerification(request.getCaptchaVerification() != null
                        ? request.getCaptchaVerification() : "")
                .build();
        captchaHelper.check(captchaRequest);
        log.debug("验证码二次校验成功, uuid: {}", request.getUuid());
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



        log.debug("保存在线用户信息, userId: {}, token: {}", sysUser.getUserId(), token);
    }
}
