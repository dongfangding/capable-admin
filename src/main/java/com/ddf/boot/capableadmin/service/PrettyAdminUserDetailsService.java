package com.ddf.boot.capableadmin.service;

import com.ddf.boot.capableadmin.mapper.SysMenuMapper;
import com.ddf.boot.capableadmin.mapper.SysRoleMapper;
import com.ddf.boot.capableadmin.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Pretty-Admin用户详情服务
 * </p>
 * 负责加载用户详细信息(角色、权限等)
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 14:55
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class PrettyAdminUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;

    /**
     * 根据用户名加载用户(Spring Security标准接口)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return loadUserDetail(sysUser);
    }

    /**
     * 根据用户ID加载用户详情
     *
     * @param userId 用户ID
     * @return UserDetails
     */
    public PrettyAdminUserDetails loadUserById(Long userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户不存在: ID=" + userId);
        }
        return loadUserDetail(sysUser);
    }

    private PrettyAdminUserDetails loadUserDetail(SysUser sysUser) {
        Long userId = sysUser.getUserId();

        // 1. 加载角色名称列表
        Set<String> roles = sysRoleMapper.findRoleNamesByUserId(userId);
        if (roles == null) {
            roles = Set.of();
        }

        // 2. 加载权限标识列表
        Set<String> permissions = sysMenuMapper.findPermissionsByUserId(userId);
        if (permissions == null) {
            permissions = Set.of();
        }

        return new PrettyAdminUserDetails(sysUser, roles, permissions);
    }
}
