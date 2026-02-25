package com.ddf.boot.capableadmin.config.interceptor;

import com.ddf.boot.common.api.exception.UnauthorizedException;
import com.ddf.boot.capableadmin.config.annotation.RequirePermission;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.util.PrettyAdminSecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * <p>
 * 权限校验拦截器
 * </p>
 * 基于RequirePermission注解进行权限校验
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 15:15
 */
public class PrettyAdminPermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        RequirePermission annotation = method.getAnnotation(RequirePermission.class);

        if (annotation == null) {
            return true;
        }

        PrettyAdminUserDetails user = PrettyAdminSecurityUtils.getCurrentUser();
        if (user == null) {
            throw new UnauthorizedException("用户未登录");
        }

        String[] permissions = annotation.value();
        if (permissions.length == 0) {
            return true;
        }

        Set<String> userPermissions = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (annotation.logical() == RequirePermission.Logical.OR) {
            for (String permission : permissions) {
                if (userPermissions.contains(permission)) {
                    return true;
                }
            }
            throw new UnauthorizedException("权限不足: " + String.join(", ", permissions));
        } else {
            // AND
            for (String permission : permissions) {
                if (!userPermissions.contains(permission)) {
                    throw new UnauthorizedException("权限不足: 需要所有权限 " + String.join(", ", permissions));
                }
            }
        }

        return true;
    }
}
