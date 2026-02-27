package com.ddf.boot.capableadmin.service;

import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.infra.mapper.SysMenuMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.model.cqrs.SysRoleAdminResult;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysRole;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.common.api.exception.BusinessException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
@RequiredArgsConstructor
public class PrettyAdminUserDetailsService {

	private final SysUserMapper sysUserMapper;
	private final SysRoleMapper sysRoleMapper;
	private final SysMenuMapper sysMenuMapper;


	/**
	 * 根据用户ID加载用户详情
	 *
	 * @param userId 用户ID
	 * @return UserDetails
	 */
	public PrettyAdminUserDetails loadUserById(Long userId) {
		SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
		if (sysUser == null) {
			throw new BusinessException(PrettyAdminExceptionCode.USER_NOT_NOT_EXISTS, userId);
		}
		return loadUserDetail(sysUser);
	}

	private PrettyAdminUserDetails loadUserDetail(SysUser sysUser) {
		Long userId = sysUser.getUserId();

		// 1. 加载角色名称列表
		Set<SysRoleAdminResult> roleList = sysRoleMapper.findRoleNamesByUserId(userId);
		Set<String> roleNames = new HashSet<>();
		boolean isAdmin = false;
		if (!CollectionUtils.isEmpty(roleList)) {
			roleNames = roleList
					.stream()
					.map(SysRoleAdminResult::getName)
					.collect(Collectors.toSet());
			isAdmin = roleList
					.stream()
					.anyMatch(SysRoleAdminResult::getIsAdmin);
		}

		Set<String> permissions = new HashSet<>();
		// 2. 加载权限标识列表
		if (isAdmin) {
			permissions = sysMenuMapper.findAllPermissions();
		} else {
			permissions = sysMenuMapper.findPermissionsByUserId(userId);
			if (permissions == null) {
				permissions = Set.of();
			}
		}
		return new PrettyAdminUserDetails(sysUser, roleNames, permissions);
	}
}
