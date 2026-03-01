package com.ddf.boot.capableadmin.service;

import cn.hutool.core.collection.CollUtil;
import com.ddf.boot.capableadmin.enums.MenuTypeEnum;
import com.ddf.boot.capableadmin.enums.PrettyAdminExceptionCode;
import com.ddf.boot.capableadmin.infra.mapper.SysMenuMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysRoleMenuMapper;
import com.ddf.boot.capableadmin.model.entity.SysMenu;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuListQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.BuildMenuRouteNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuRes;
import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.api.util.JsonUtil;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.TreeConvertUtil;
import com.ddf.boot.common.mvc.permissionscan.PermissionMenuType;
import com.ddf.boot.common.mvc.permissionscan.ScanPermissionPayload;
import com.ddf.boot.common.mvc.permissionscan.SysMenuFunction;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * description
 * </p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 20:28
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysMenuService {

	private final SysMenuMapper sysMenuMapper;
	private final SysRoleMenuMapper sysRoleMenuMapper;
	private final PrettyAdminCacheManager cacheManager;

	/**
	 * 保存菜单
	 *
	 * @param request
	 */
	@Transactional(rollbackFor = Exception.class)
	public void persist(SysMenuCreateRequest request) {
		// 是否更新的的判定， 传menuId即为更新
		boolean isUpdate = Objects.nonNull(request.getMenuId());
		SysMenu oldMenu = null;
		if (isUpdate) {
			oldMenu = sysMenuMapper.selectByPrimaryKey(request.getMenuId());
			if (Objects.isNull(oldMenu)) {
				throw new BusinessException(PrettyAdminExceptionCode.MENU_NOT_EXISTS);
			}
		}
		final SysMenu byTitle = sysMenuMapper.findByTitle(request.getTitle());
		if (Objects.nonNull(byTitle) && (!Objects.equals(byTitle.getMenuId(), request.getMenuId())) || (isUpdate
				&& !Objects.equals(byTitle.getMenuId(), request.getMenuId()))) {
			throw new BusinessException(PrettyAdminExceptionCode.MENU_TITLE_EXISTS);
		}
		if (StringUtils.isNotBlank(request.getName())) {
			final SysMenu byName = sysMenuMapper.findByName(request.getName());
			if (Objects.nonNull(byName) && (!Objects.equals(byName.getMenuId(), request.getMenuId())) || (isUpdate
					&& !Objects.equals(byName.getMenuId(), request.getMenuId()))) {
				throw new BusinessException(PrettyAdminExceptionCode.MENU_NAME_EXISTS);
			}
		}
		final Long newPid = request.getPid();
		if (Long
				.valueOf(0L)
				.equals(newPid)) {
			request.setPid(null);
		}

		Long affectedMenuId = null;
		if (!isUpdate) {
			final SysMenu sysMenu = BeanCopierUtils.copy(request, SysMenu.class);
			sysMenu.setType(request.getType());
			sysMenuMapper.insertSelective(sysMenu);
			// 更新父节点菜单数目
			updateSubCount(newPid, true);
		} else {
			affectedMenuId = request.getMenuId();
			final Long oldPid = oldMenu.getPid();
			BeanCopierUtils.copy(request, oldMenu);
			sysMenuMapper.updateByPrimaryKeySelective(oldMenu);
			if (Objects.nonNull(oldPid)) {
				if (!oldPid.equals(newPid)) {
					// 更新原父节点菜单数目
					updateSubCount(oldPid, true);
				}
			}
		}
		// 更新新父节点菜单数目
		updateSubCount(newPid, true);

		// 清理缓存
		if (affectedMenuId != null) {
			cacheManager.cleanMenuCache(affectedMenuId);
		}
	}

	/**
	 * 菜单列表查询，默认查询一级菜单，然后展开父节点的时候，再次调用该接口传入pid返回明细
	 * 1. 菜单列表时, 传入pid=0，只展示一级菜单列表
	 * 2. 展开菜单列表时， 传入pid=选择的菜单id的pid, 返回子节点，懒加载方式
	 *
	 * @param query
	 * @return
	 */
	public List<SysMenuRes> list(SysMenuListQuery query) {
		final List<SysMenu> sysMenus = sysMenuMapper.list(query);
		return BeanCopierUtils.copy(sysMenus, SysMenuRes.class);
	}

	/**
	 * 所有菜单树
	 *
	 * @return
	 */
	public List<BuildMenuRouteNode> allTree() {
		final List<SysMenu> sysMenus = sysMenuMapper.list(new SysMenuListQuery());
		return buildMenuTree(sysMenus);
	}

	/**
	 * 删除菜单以及遍历子节点
	 *
	 * @param menuIdSet
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delete(Set<Long> menuIdSet) {
		// 查询要删除节点的一级子节点
		final List<SysMenu> oneLevelChildren = sysMenuMapper.findByPidList(menuIdSet);
		// 查询要删除节点的自身信息
		final List<SysMenu> selfMenu = sysMenuMapper.findByIds(menuIdSet);
		final List<SysMenu> waitDeleteMenuList = new ArrayList<>(oneLevelChildren);
		waitDeleteMenuList.addAll(selfMenu);
		// 所有要删除的菜单
		final Set<SysMenu> allWaitDeleteMenus = getChildMenus(waitDeleteMenuList, new HashSet<>());
		final Set<Long> allWaitDeleteMenuIdSet = allWaitDeleteMenus
				.stream()
				.map(SysMenu::getMenuId)
				.collect(Collectors.toSet());
		final int i = sysMenuMapper.deleteByMenuIds(allWaitDeleteMenuIdSet);
		if (i > 0) {
			// 更新所有当前节点的父节点的子节点数量
			final Set<Long> pidList = allWaitDeleteMenus
					.stream()
					.map(SysMenu::getPid)
					.collect(Collectors.toSet());
			pidList.forEach(pid -> {
				updateSubCount(pid, true);
			});
			// 删除所有当前节点的角色菜单关系
			sysRoleMenuMapper.deleteByMenuIdList(allWaitDeleteMenuIdSet);

			// 批量清理缓存
			cacheManager.cleanMenuCacheBatch(allWaitDeleteMenuIdSet);
		}
	}

	/**
	 * 获取部门同级别和上级节点数据
	 *
	 * @param query
	 * @return
	 */
	public List<SysMenuNode> fetchSameAndSuperiorData(SysMenuSuperiorQuery query) {
		final Long menuId = query.getMenuId();
		final boolean excludeSelfAndSub = query.isExcludeSelfAndSub();
		final SysMenu dept = sysMenuMapper.selectByPrimaryKey(menuId);
		if (Objects.isNull(dept)) {
			return new ArrayList<>();
		}
		List<SysMenu> superior = getSuperior(dept, new ArrayList<>());
		if (excludeSelfAndSub) {
			for (SysMenu data : superior) {
				// 这种情况似乎不会出现？为了避免异常数据？
				if (data
						.getMenuId()
						.equals(dept.getPid())) {
					data.setSubCount(data.getSubCount() - 1);
				}
			}
			// 编辑时不显示自己以及自己下级的数据，避免出现PID数据环形问题(似乎只是为了过滤自己，下级数据问题在查询源头上已经避免了？)
			superior = superior
					.stream()
					.filter(i -> !menuId.equals(i.getMenuId()))
					.toList();
		}
		final List<SysMenuNode> originNodeList = BeanCopierUtils.copy(superior, SysMenuNode.class);
		return TreeConvertUtil.convert(originNodeList);
	}

	/**
	 * 构建用户菜单树
	 *
	 * @return
	 */
	public List<BuildMenuRouteNode> buildUserMenuTree(Long userId) {
		boolean isAdmin = Objects.equals(1L, userId);
		List<SysMenu> userMenuList;
		if (isAdmin) {
			userMenuList = sysMenuMapper.getAdminUserAllMenuExcludeBtn();
		} else {
			userMenuList = sysMenuMapper.getUserAllMenuExcludeBtn(userId);
		}
		return buildMenuTree(userMenuList);
	}

	/**
	 * 构建用户菜单树
	 *
	 * @return
	 */
	public List<BuildMenuRouteNode> buildMenuTree(List<SysMenu> userMenuList) {
		if (CollUtil.isEmpty(userMenuList)) {
			return Lists.newArrayList();
		}
		List<BuildMenuRouteNode> nodeList = new ArrayList<>();
		for (SysMenu menu : userMenuList) {
			final BuildMenuRouteNode node = new BuildMenuRouteNode();
			node.setMenuId(menu.getMenuId());
			node.setPid(menu.getPid());
			node.setName(StringUtils.defaultIfBlank(menu.getName(), menu.getTitle()));
			// 一级目录需要加斜杠，不然会报警告
			node.setPath(menu.getPath());
			node.setComponent(menu.getComponent());
			node.setMeta(JsonUtil.toBean(menu.getMeta(), Map.class));
			node.setChildren(new ArrayList<>());
			node.setType(menu.getType());
			node.setIcon(menu.getIcon());
			node.setPermission(menu.getPermission());
			node.setEnable(menu.getEnable());
			nodeList.add(node);
		}
		return TreeConvertUtil.convert(nodeList);
	}

	/**
	 * 获取当前节点同级别节点和上级节点，用于编辑节点时查看当前节点时使用
	 *
	 * @param dept
	 * @param menuList
	 * @return
	 */
	private List<SysMenu> getSuperior(SysMenu dept, List<SysMenu> menuList) {
		menuList.addAll(sysMenuMapper.findByPid(dept.getPid()));
		if (dept.getPid() == 0) {
			return menuList;
		}
		return getSuperior(sysMenuMapper.selectByPrimaryKey(dept.getPid()), menuList);
	}

	/**
	 * 递归获取子节点
	 *
	 * @param menuList
	 * @param menuSet
	 * @return
	 */
	private Set<SysMenu> getChildMenus(List<SysMenu> menuList, Set<SysMenu> menuSet) {
		final Set<Long> menuIdList = menuList
				.stream()
				.map(SysMenu::getMenuId)
				.collect(Collectors.toSet());
		final List<SysMenu> list = sysMenuMapper.findByPidList(menuIdList);
		if (CollUtil.isNotEmpty(list)) {
			menuSet.addAll(list);
			getChildMenus(list, menuSet);
		}
		return menuSet;
	}

	/**
	 * 更新父节点下的子节点的数量
	 *
	 * @param menuId
	 * @param clearCountIfZero
	 * @return
	 */
	public void updateSubCount(Long menuId, boolean clearCountIfZero) {
		final int count = sysMenuMapper.countByPid(menuId);
		if (clearCountIfZero) {
			sysMenuMapper.updateSubCount(menuId, count);
		} else if (count > 0) {
			sysMenuMapper.updateSubCount(menuId, count);
		}
	}

	/**
	 * 自动创建菜单
	 *
	 * @param payload
	 */
	public void autoCreateMenu(ScanPermissionPayload payload) {
		final List<SysMenuFunction> functions = payload.getMenuFunctions();
		if (CollectionUtils.isEmpty(functions)) {
			log.info("未扫描到菜单信息，无需自动创建菜单");
			return;
		}

		Map<String, SysMenu> menuCacheMap = new HashMap<>();
		List<SysMenu> saveList = new ArrayList<>();
		Set<Long> updatedParentIds = new HashSet<>();
		for (SysMenuFunction function : functions) {
			recursiveCreateMenu(function, null, menuCacheMap, saveList, updatedParentIds);
		}
		sysMenuMapper.batchInsert(saveList);
		for (Long parentId : updatedParentIds) {
			updateSubCount(parentId, true);
		}
	}

	private void recursiveCreateMenu(SysMenuFunction function, SysMenu parentMenu, Map<String, SysMenu> menuCacheMap,
			List<SysMenu> saveList, Set<Long> updatedParentIds) {
		// 判断当前节点是否已存在
		SysMenu existing = sysMenuMapper.findByTitle(function.getCode());
		SysMenu currentMenu;
		if (existing != null) {
			currentMenu = existing;
			// 可选：更新权限或其他属性
			// if (!Objects.equals(existing.getPermission(), function.getPermission())) {
			// existing.setPermission(function.getPermission());
			// update(existing);
			// }

		} else {
			currentMenu = new SysMenu();
			currentMenu.setTitle(function.getCode());
			currentMenu.setName(function.getCode());
			currentMenu.setSort(function.getSort());
			currentMenu.setComponent(function.getCode());
			currentMenu.setPath("");
			currentMenu.setType(convertMenuType(function.getType()));
			currentMenu.setPermission(function.getPermission());
			currentMenu.setSubCount(0);
			if (parentMenu != null) {
				currentMenu.setPid(parentMenu.getMenuId());
			}
			// 因为有绑定关系，这里如果不保存，后面子节点拿不到父类的id，导致绑定不上，只能每处理一次都保存了
			// saveList.add(currentMenu);
			sysMenuMapper.insertSelective(currentMenu);
		}

		// 缓存当前节点，方便后面查找子节点的父级
		menuCacheMap.put(function.getCode(), currentMenu);

		if (parentMenu != null) {
			updatedParentIds.add(parentMenu.getMenuId());
		}

		// 递归处理子节点
		if (!CollectionUtils.isEmpty(function.getChildren())) {
			for (SysMenuFunction child : function.getChildren()) {
				recursiveCreateMenu(child, currentMenu, menuCacheMap, saveList, updatedParentIds);
			}
		}
	}

	private String convertMenuType(PermissionMenuType menuType) {
		if (Objects.equals(PermissionMenuType.CATELOG, menuType)) {
			return MenuTypeEnum.CATALOG.getValue();
		} else if (Objects.equals(PermissionMenuType.MENU, menuType)) {
			return MenuTypeEnum.MENU.getValue();
		}  else if (Objects.equals(PermissionMenuType.BUTTON, menuType)) {
			return MenuTypeEnum.BUTTON.getValue();
		}  else if (Objects.equals(PermissionMenuType.EMBEDDED, menuType)) {
			return MenuTypeEnum.EMBEDDED.getValue();
		}   else if (Objects.equals(PermissionMenuType.LINK, menuType)) {
			return MenuTypeEnum.LINK.getValue();
		} else {
			throw new IllegalArgumentException("菜单类型参数不可转换");
		}
	}
}
