package com.ddf.boot.capableadmin.infra.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import com.ddf.boot.common.api.model.authentication.UserClaim;
import com.ddf.boot.common.api.util.DateUtils;
import com.ddf.boot.common.core.authentication.TokenUtil;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * Web MVC 配置
 * </p>
 * 注册拦截器
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 15:25
 */
@Configuration
public class PrettyAdminWebConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
		registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
				.addPathPatterns("/capable-admin/**")
				.excludePathPatterns("/capable-admin/admin/auth/login", "/error");
	}

	/**
	 * 配置 CORS 跨域
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/capable-admin/**")
				.allowedOriginPatterns("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
	}

	/**
	 * 重写 Sa-Token 框架内部算法策略
	 */
	@PostConstruct
	public void rewriteSaStrategy() {
		// 重写 Token 生成策略
		SaStrategy.instance.createToken = (loginId, loginType) -> {
			final UserClaim claim = new UserClaim();
			claim.setUserId(loginId.toString());
			claim.setProperties(Map.of("ctime", DateUtils.currentTimeSeconds()));
			return TokenUtil.createToken(claim).getToken();
		};
	}
}
