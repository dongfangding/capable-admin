package com.ddf.boot.capableadmin.config;

import com.ddf.boot.capableadmin.config.interceptor.PrettyAdminPermissionInterceptor;
import org.springframework.context.annotation.Configuration;
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
        // 注册权限拦截器
        registry.addInterceptor(new PrettyAdminPermissionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/admin/auth/login",
                        "/admin/auth/code",
                        "/admin/auth/logout",
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**");
    }
}
