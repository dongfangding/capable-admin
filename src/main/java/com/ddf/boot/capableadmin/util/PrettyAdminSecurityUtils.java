package com.ddf.boot.capableadmin.util;

import com.ddf.boot.common.api.exception.UnauthorizedException;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * 安全工具类
 * </p>
 * 提供当前用户信息访问
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 14:45
 */
public class PrettyAdminSecurityUtils {

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        PrettyAdminUserDetails user = getCurrentUser();
        if (user == null) {
            throw new UnauthorizedException("用户未登录");
        }
        return user.getUserId();
    }

    /**
     * 获取当前用户详情
     *
     * @return 用户详情
     */
    public static PrettyAdminUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof PrettyAdminUserDetails) {
            return (PrettyAdminUserDetails) principal;
        }
        return null;
    }

    /**
     * 获取当前用户名
     *
     * @return 用户名
     */
    public static String getCurrentUsername() {
        PrettyAdminUserDetails user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 检查当前用户是否有指定权限
     *
     * @param permission 权限标识
     * @return 是否有权限
     */
    public static boolean hasPermission(String permission) {
        PrettyAdminUserDetails user = getCurrentUser();
        if (user == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (authorities == null) {
            return false;
        }
        return authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals(permission));
    }

    /**
     * 检查当前用户是否有任意一个指定权限
     *
     * @param permissions 权限标识列表
     * @return 是否有任意权限
     */
    public static boolean hasAnyPermission(String... permissions) {
        PrettyAdminUserDetails user = getCurrentUser();
        if (user == null) {
            return false;
        }
        for (String permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查当前用户是否有所有指定权限
     *
     * @param permissions 权限标识列表
     * @return 是否有所有权限
     */
    public static boolean hasAllPermissions(String... permissions) {
        PrettyAdminUserDetails user = getCurrentUser();
        if (user == null) {
            return false;
        }
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
}
