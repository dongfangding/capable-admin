package com.ddf.boot.capableadmin.service;

import cn.hutool.core.collection.CollUtil;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMenuMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserRoleMapper;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysRole;
import com.ddf.boot.capableadmin.model.request.sys.EnableRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleMenuUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysRoleRes;
import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PageUtil;
import com.ddf.boot.common.core.util.PreconditionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统角色
 * </p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/07 17:36
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysRoleService {

	private final SysRoleMapper sysRoleMapper;
	private final SysRoleMenuMapper sysRoleMenuMapper;
	private final SysUserRoleMapper sysUserRoleMapper;
	private final PrettyAdminCacheManager cacheManager;

	/**
	 * 查询所有角色
	 *
	 * @return
	 */
	public List<SysRoleRes> listAll() {
		final List<SysRole> roles = sysRoleMapper.listAll(new SysRoleListRequest());
		return CollUtil.isNotEmpty(roles) ? BeanCopierUtils.copy(roles, SysRoleRes.class) : new ArrayList<>();
	}

	/**
	 * 保存角色
	 *
	 * @param request
	 */
	@Transactional(rollbackFor = Exception.class)
	public void persist(SysRoleCreateRequest request) {
		if (Objects.isNull(request.getRoleId())) {
			PreconditionUtil.checkArgument(
					Objects.isNull(sysRoleMapper.findByRoleName(request.getName())), "角色名称已存在");
			final SysRole role = BeanCopierUtils.copy(request, SysRole.class);
			sysRoleMapper.insertSelective(role);
		} else {
			final SysRole role = sysRoleMapper.selectByPrimaryKey(request.getRoleId());
			PreconditionUtil.checkArgument(Objects.nonNull(role), "记录不存在");
			SysRole tmpRole;
			if (!role
					.getName()
					.equals(request.getName()) && (Objects.nonNull(
					tmpRole = sysRoleMapper.findByRoleName(request.getName()))) && !tmpRole
					.getRoleId()
					.equals(request.getRoleId())) {
				throw new BusinessException("角色名称已存在");
			}
			BeanCopierUtils.copy(request, role);
			sysRoleMapper.updateByPrimaryKeySelective(role);

			// 清理缓存
			cacheManager.cleanRoleCache(request.getRoleId());
		}
	}

	/**
	 * 更新角色菜单
	 *
	 * @param request
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateRoleMenu(SysRoleMenuUpdateRequest request) {
		sysRoleMenuMapper.deleteByRoleId(request.getRoleId());
		sysRoleMenuMapper.insertRoleMenu(request.getRoleId(), request.getMenuIds());

		// 清理缓存（这是权限变更的关键）
		cacheManager.cleanRoleCache(request.getRoleId());
	}

	/**
	 * 删除角色
	 *
	 * @param roleId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Long roleId) {
		PreconditionUtil.checkArgument(!isRelatedUser(Set.of(roleId)), PrettyAdminExceptionCode.ROLE_HAS_RELATED_USER);
		final SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
		if (role.getIsAdmin()) {
			throw new BusinessException(PrettyAdminExceptionCode.LESS_TARGET_PERMISSION_LEVEL);
		}
		final PrettyAdminUserDetails currentUser = PrettyAdminSecurityUtils.getCurrentUser();
		if (currentUser.getMaxAuthorityRoleLevel() > role.getLevel()) {
			throw new BusinessException(PrettyAdminExceptionCode.LESS_TARGET_PERMISSION_LEVEL);
		}

		// 删除角色
		sysRoleMapper.deleteByRoleIds(Set.of(roleId));
		// 删除角色关联的菜单
		sysRoleMenuMapper.deleteByRoleIds(Set.of(roleId));
		// 批量清理缓存
		cacheManager.cleanRoleCacheBatch(Set.of(roleId));
	}

	/**
	 * 角色是否关联用户
	 *
	 * @param roleIdSet
	 * @return
	 */
	private boolean isRelatedUser(Set<Long> roleIdSet) {
		return sysUserRoleMapper.countByRoleIds(roleIdSet) > 0;
	}


	/**
	 * 列表
	 *
	 * @param request
	 * @return
	 */
	public PageResult<SysRoleRes> list(SysRoleListRequest request) {
		return PageUtil.startPage(request, () -> sysRoleMapper.listAll(request));
	}


	/**
	 * 更新启用状态
	 *
	 * @param enableRequest
	 * @return
	 */
	public Boolean updateEnable(EnableRequest enableRequest) {
		return sysRoleMapper.updateEnable(enableRequest.getId(), enableRequest.getEnable()) > 0;
	}
}
