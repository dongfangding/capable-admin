package com.ddf.boot.capableadmin.service;

import cn.hutool.core.collection.CollUtil;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMenuMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserRoleMapper;
import com.ddf.boot.capableadmin.infra.repository.SysMenuRepository;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
	private final SysMenuRepository sysMenuRepository;

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
		SysRole role;
		if (Objects.isNull(request.getRoleId())) {
			PreconditionUtil.checkArgument(
					Objects.isNull(sysRoleMapper.findByRoleName(request.getName())), "角色名称已存在");
			role = BeanCopierUtils.copy(request, SysRole.class);
			sysRoleMapper.insertSelective(role);
		} else {
			role = sysRoleMapper.selectByPrimaryKey(request.getRoleId());
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
		}
		final SysRoleMenuUpdateRequest roleMenuUpdateRequest = new SysRoleMenuUpdateRequest();
		roleMenuUpdateRequest.setRoleId(role.getRoleId());
		roleMenuUpdateRequest.setMenuIds(request.getMenuIds());
		updateRoleMenu(roleMenuUpdateRequest);
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

		final String menuIds = request
				.getMenuIds()
				.stream()
				.map(String::valueOf)
				.collect(Collectors.joining(","));
		sysRoleMapper.updateRoleMenuIds(request.getRoleId(), menuIds);

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
		final PageResult<SysRoleRes> result = PageUtil.startPage(request, () -> sysRoleMapper.listAll(request), (list) -> BeanCopierUtils.copy(list, SysRoleRes.class));
		final List<SysRoleRes> content = result.getContent();
		final boolean hasAdmin = content
				.stream()
				.anyMatch(SysRoleRes::getIsAdmin);
		if (hasAdmin) {
			final Set<Long> allMenuIds = sysMenuRepository.getAllMenuIds();
			for (SysRoleRes res : content) {
				if (Objects.equals(Boolean.TRUE, res.getIsAdmin())) {
					res.setMenuIds(allMenuIds);
				}
			}
		}
		return result;
	}


	/**
	 * 更新启用状态
	 *
	 * @param enableRequest
	 * @return
	 */
	public Boolean updateEnable(EnableRequest enableRequest) {
		return sysRoleMapper.updateEnable(enableRequest.getId(), enableRequest.getEnabled()) > 0;
	}
}
