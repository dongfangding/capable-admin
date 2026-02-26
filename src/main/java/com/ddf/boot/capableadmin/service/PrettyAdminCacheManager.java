package com.ddf.boot.capableadmin.service;

import com.ddf.boot.capableadmin.enums.PrettyAdminRedisKeyEnum;
import com.ddf.boot.capableadmin.infra.mapper.SysMenuMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.common.redis.helper.RedisCommandHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 统一缓存管理器
 * </p>
 * 负责管理所有业务缓存的生命周期
 * 核心功能:
 * 1. 用户登录时刷新缓存
 * 2. 菜单/角色变更时清理相关缓存
 * 3. 级联清理关联缓存
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 14:25
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PrettyAdminCacheManager {

    private final RedisCommandHelper redisCommandHelper;
    private final SysUserMapper sysUserMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMapper sysRoleMapper;

    /**
     * 用户登录时清理并重建缓存
     * 
     * @param userId 用户ID
     */
    public void refreshUserCache(Long userId) {
        log.info("刷新用户缓存, userId: {}", userId);
        // 清理用户相关的所有缓存
        cleanUserAllCache(userId);
    }

    /**
     * 清理用户所有相关缓存
     * 核心方法:确保用户相关的所有缓存都被清理
     * 
     * @param userId 用户ID
     */
    public void cleanUserAllCache(Long userId) {
        String userIdStr = String.valueOf(userId);

        // 1. 清理用户基本信息缓存
        String userInfoKey = PrettyAdminRedisKeyEnum.USER_INFO.getKey(userIdStr);
        redisCommandHelper.delete(userInfoKey);

        // 2. 清理用户详情缓存
        String userDetailsKey = PrettyAdminRedisKeyEnum.USER_DETAILS.getKey(userIdStr);
        redisCommandHelper.delete(userDetailsKey);

        // 3. 清理用户菜单树缓存
        String userMenuTreeKey = PrettyAdminRedisKeyEnum.USER_MENU_TREE.getKey(userIdStr);
        redisCommandHelper.delete(userMenuTreeKey);

        // 4. 清理用户权限缓存
        String userPermissionsKey = PrettyAdminRedisKeyEnum.USER_PERMISSIONS.getKey(userIdStr);
        redisCommandHelper.delete(userPermissionsKey);

        log.info("用户缓存清理完成, userId: {}", userId);
    }

    /**
     * 菜单变更时清理相关缓存
     * 核心逻辑:找到所有受影响的用户,清理他们的菜单缓存
     * 
     * @param menuId 菜单ID
     */
    public void cleanMenuCache(Long menuId) {
        log.info("清理菜单缓存, menuId: {}", menuId);

        // 1. 清理菜单本身缓存
        String menuInfoKey = PrettyAdminRedisKeyEnum.MENU_INFO.getKey(String.valueOf(menuId));
        redisCommandHelper.delete(menuInfoKey);

        // 2. 查询受影响的用户ID列表
        List<Long> affectedUserIds = findAffectedUsersByMenu(menuId);

        // 3. 清理受影响用户的菜单缓存
        affectedUserIds.forEach(userId -> {
            String userMenuTreeKey = PrettyAdminRedisKeyEnum.USER_MENU_TREE.getKey(String.valueOf(userId));
            redisCommandHelper.delete(userMenuTreeKey);

            // 同时清理用户权限缓存
            String userPermissionsKey = PrettyAdminRedisKeyEnum.USER_PERMISSIONS.getKey(String.valueOf(userId));
            redisCommandHelper.delete(userPermissionsKey);
        });

        log.info("菜单缓存清理完成, menuId: {}, 影响用户数: {}", menuId, affectedUserIds.size());
    }

    /**
     * 角色变更时清理相关缓存
     * 
     * @param roleId 角色ID
     */
    public void cleanRoleCache(Long roleId) {
        log.info("清理角色缓存, roleId: {}", roleId);

        // 1. 清理角色本身缓存
        String roleInfoKey = PrettyAdminRedisKeyEnum.ROLE_INFO.getKey(String.valueOf(roleId));
        redisCommandHelper.delete(roleInfoKey);

        // 2. 清理角色权限缓存
        String rolePermissionsKey = PrettyAdminRedisKeyEnum.ROLE_PERMISSIONS.getKey(String.valueOf(roleId));
        redisCommandHelper.delete(rolePermissionsKey);

        // 3. 查询受影响的用户,清理用户缓存
        List<Long> affectedUserIds = findAffectedUsersByRole(roleId);
        affectedUserIds.forEach(this::cleanUserAllCache);

        log.info("角色缓存清理完成, roleId: {}, 影响用户数: {}", roleId, affectedUserIds.size());
    }

    /**
     * 批量清理菜单缓存
     * 
     * @param menuIds 菜单ID集合
     */
    public void cleanMenuCacheBatch(Set<Long> menuIds) {
        menuIds.forEach(this::cleanMenuCache);
    }

    /**
     * 批量清理角色缓存
     * 
     * @param roleIds 角色ID集合
     */
    public void cleanRoleCacheBatch(Set<Long> roleIds) {
        roleIds.forEach(this::cleanRoleCache);
    }

    /**
     * 清理在线用户缓存
     * 
     * @param token Token
     */
    public void cleanOnlineUserCache(String token) {
        String onlineUserKey = PrettyAdminRedisKeyEnum.ONLINE_USER.getKey(token);
        redisCommandHelper.delete(onlineUserKey);
        log.debug("清理在线用户缓存, token: {}", token);
    }

    /**
     * 查询受菜单影响的用户ID列表
     * 
     * @param menuId 菜单ID
     * @return 用户ID列表
     */
    private List<Long> findAffectedUsersByMenu(Long menuId) {
        // 通过菜单ID查询关联的角色ID
        List<Long> roleIds = sysRoleMapper.findRoleIdsByMenuId(menuId);
        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 通过角色ID查询关联的用户ID
        List<Long> userIds = sysUserMapper.findUserIdsByRoleIds(roleIds);
        return userIds != null ? userIds : new ArrayList<>();
    }

    /**
     * 查询受角色影响的用户ID列表
     * 
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    private List<Long> findAffectedUsersByRole(Long roleId) {
        List<Long> userIds = sysUserMapper.findUserIdsByRoleId(roleId);
        return userIds != null ? userIds : new ArrayList<>();
    }
}
