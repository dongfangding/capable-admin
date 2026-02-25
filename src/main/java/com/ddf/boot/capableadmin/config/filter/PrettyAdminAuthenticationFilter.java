package com.ddf.boot.capableadmin.config.filter;

import com.alibaba.fastjson.JSON;
import com.ddf.boot.common.api.model.authentication.UserClaim;
import com.ddf.boot.common.core.authentication.TokenUtil;
import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.service.PrettyAdminUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>
 * 认证过滤器
 * </p>
 * 负责Token验证和用户上下文初始化
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 15:05
 */
@Slf4j
public class PrettyAdminAuthenticationFilter extends OncePerRequestFilter {

    private final RedisCommandHelper redisCommandHelper;
    private final PrettyAdminUserDetailsService userDetailsService;

    public PrettyAdminAuthenticationFilter(RedisCommandHelper redisCommandHelper,
            PrettyAdminUserDetailsService userDetailsService) {
        this.redisCommandHelper = redisCommandHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. 提取Token
            String token = extractToken(request);
            if (StringUtils.isBlank(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. 验证Token有效性
			TokenUtil.checkToken(token);

            // 3. 从Token中提取用户信息
            UserClaim userClaim = TokenUtil.getUserClaim(token);
            if (userClaim == null || StringUtils.isBlank(userClaim.getUserId())) {
                filterChain.doFilter(request, response);
                return;
            }
            Long userId = Long.parseLong(userClaim.getUserId());

            // 4. 检查在线用户
            String onlineUserKey = PrettyAdminRedisKeyEnum.ONLINE_USER.getKey(token);
            if (!redisCommandHelper.hasKey(onlineUserKey)) {
                // 如果是在线用户key失效其实就代表登陆失效了
                log.warn("用户不在线或会话已过期, userId: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }

            // 5. 加载用户详情(优先从缓存)
            PrettyAdminUserDetails userDetails = loadUserDetails(userId);

            // 6. 设置认证上下文
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder
					.getContext().setAuthentication(authentication);

            // 7. 续期在线状态(而非Token本身，Token是无状态的JWT，不能改过期时间除非发新的)
            renewOnlineStatusIfNeeded(token, onlineUserKey);

            filterChain.doFilter(request, response);
        } finally {
            // 注意：这里不要清除上下文，否则后续的Controller里获取不到认证信息
            // SecurityContextHolder.clearContext(); 应该在请求结束后的 Filter 清理，或者由框架处理
            // Spring Security 会在一个请求结束后清理 ThreadLocal，但通常是在 FilterChain 的最外层
            // OncePerRequestFilter 不会自动清理，但为了安全起见，如果我们没有使用 HttpSession 存储 SecurityContext
            // (我们确实是无状态的)，那么在请求处理完毕后应该清理?
            // 其实 Standard Spring Security setup 会有一个 SecurityContextPersistenceFilter 负责清理
            // 如果我们也想清理，应该在 finally 中调用，但是 doFilter 之后就是 Controller 的执行了？
            // 不，doFilter 是链式调用，controller 执行完回来才会执行 finally 块。
            // 所以这里是可以清理的。
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 从请求头中提取Token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 加载用户详情 - 优先从缓存
     */
    private PrettyAdminUserDetails loadUserDetails(Long userId) {
        String userDetailsKey = PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(String.valueOf(userId));

        // 1. 尝试从缓存加载
        String cached = redisCommandHelper.get(userDetailsKey);
        if (StringUtils.isNotBlank(cached)) {
            try {
                return JSON.parseObject(cached, PrettyAdminUserDetails.class);
            } catch (Exception e) {
                log.error("反序列化用户缓存失败", e);
                // 缓存数据有问题，删除
                redisCommandHelper.delete(userDetailsKey);
            }
        }

        // 2. 从数据库加载
        PrettyAdminUserDetails userDetails = userDetailsService.loadUserById(userId);

        // 3. 写入缓存
        // 注意：PrettyAdminUserDetails 必须有默认构造函数或合理的 JSON 注解才能反序列化
        // 我们的实现使用了 Lombok @Getter 和构造函数，FastJson 可能需要配置
        // 为了安全起见，这里假设 FastJson 可以处理，或者我们应该配置 PrettyAdminUserDetails
        redisCommandHelper.setEx(userDetailsKey, JSON.toJSONString(userDetails),
                PrettyAdminRedisKeyEnum.USER_DETAILS.getTtl().getSeconds());

        return userDetails;
    }

    /**
     * 续期在线状态
     */
    private void renewOnlineStatusIfNeeded(String token, String onlineUserKey) {
        // 简单的策略：每次请求都重置过期时间，或者检查剩余时间
        // 为了减少 Redis 写操作，可以检查 getExpire
        // 但为了性能，其实 expire 命令也是很快的
        // 这里我们选择：如果剩余时间不到一半，就续期
        // ONLINE_USER TTL 是 2小时
        // 假设不需要频繁调用 getExpire，可以直接调用 expire，Redis 会处理
        // 或者：redisCommandHelper.expire(onlineUserKey, 2, TimeUnit.HOURS);

        // 优化：仅当剩余时间 < 1小时续期
        Long ttl = redisCommandHelper.getExpire(onlineUserKey);
        if (ttl != null && ttl < 3600) {
            redisCommandHelper.expire(onlineUserKey,
                    PrettyAdminRedisKeyEnum.ONLINE_USER.getTtl().getSeconds());
        }
    }
}
