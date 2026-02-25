package com.ddf.boot.capableadmin.config;

import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import com.ddf.boot.capableadmin.config.filter.PrettyAdminAuthenticationFilter;
import com.ddf.boot.capableadmin.service.PrettyAdminUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * Spring Security配置
 * </p>
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 15:20
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class PrettyAdminSecurityConfig extends WebSecurityConfigurerAdapter {

    private final RedisCommandHelper redisCommandHelper;
    private final PrettyAdminUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭CSRF
        http.csrf().disable()
                // 不创建会话
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 放行静态资源
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**")
                .permitAll()
                // 放行登录接口
                .antMatchers("/admin/auth/login", "/admin/auth/code", "/admin/auth/logout").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated();

        // 添加自定义认证过滤器
        PrettyAdminAuthenticationFilter startAuthenticationFilter = new PrettyAdminAuthenticationFilter(
                redisCommandHelper, userDetailsService);
        http.addFilterBefore(startAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        http.headers().cacheControl();
    }
}
