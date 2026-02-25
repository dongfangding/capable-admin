package com.ddf.boot.capableadmin.config;


import com.ddf.boot.common.api.model.authentication.AuthenticateToken;
import com.ddf.boot.common.api.model.authentication.UserClaim;
import com.ddf.boot.common.authentication.config.AuthenticationProperties;
import com.ddf.boot.common.core.authentication.TokenCache;
import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2023/06/25 14:44
 */
@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class PrettyAdminTokenCacheImpl implements TokenCache {

	private final RedisCommandHelper redisCommandHelper;
	private final AuthenticationProperties authenticationProperties;

	/**
	 * token key
	 * %s uid
	 */
	private static final String TOKEN_KEY = "pretty-admin:authentication:token:%s";

	/**
	 * 获取token key规则
	 *
	 * @param uid
	 * @return
	 */
	public String getTokenKey(String uid) {
		return String.format(TOKEN_KEY, uid);
	}

	@Override
	public void setToken(UserClaim userClaim, AuthenticateToken authenticateToken) {
		// token存入缓存
		redisCommandHelper.setEx(
				getTokenKey(userClaim.getUserId()), authenticateToken.getToken(),
				authenticationProperties.getExpiredMinute(), TimeUnit.MINUTES
		);
	}

	@Override
	public String getToken(String userId) {
		return redisCommandHelper.get(getTokenKey(userId));
	}

	@Override
	public void refreshToken(String userId, String token) {
		// token存入缓存
		redisCommandHelper.setEx(
				getTokenKey(userId), token, authenticationProperties.getExpiredMinute(), TimeUnit.MINUTES);
	}
}
