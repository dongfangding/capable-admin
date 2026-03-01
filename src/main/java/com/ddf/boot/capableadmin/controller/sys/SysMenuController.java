package com.ddf.boot.capableadmin.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuListQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.BuildMenuRouteNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuRes;
import com.ddf.boot.capableadmin.service.SysMenuService;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.boot.common.mvc.permissionscan.PermissionMenuScanner;
import com.ddf.boot.common.mvc.permissionscan.ScanPermissionPayload;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统菜单管理
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/07 15:18
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;
    private final PermissionMenuScanner permissionMenuScanner;

    /**
     * 保存菜单
     *
     * @param request
     */
    @PostMapping("persist")
    @SaCheckPermission(value = { "menu:add", "menu:edit" })
    public ResponseData<Boolean> persist(@RequestBody @Valid SysMenuCreateRequest request) {
        sysMenuService.persist(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 菜单列表查询，默认查询一级菜单，然后展开父节点的时候，再次调用该接口传入pid返回明细
     * 1. 菜单列表时, 传入pid=0，只展示一级菜单列表
     * 2. 展开菜单列表时， 传入pid=选择的菜单id的pid, 返回子节点，懒加载方式
     *
     * @param query
     * @return
     */
    @GetMapping("list")
    @SaCheckPermission("menu:list")
    public ResponseData<List<SysMenuRes>> list(SysMenuListQuery query) {
        return ResponseData.success(sysMenuService.list(query));
    }


	/**
	 * 菜单列表查询，默认查询一级菜单，然后展开父节点的时候，再次调用该接口传入pid返回明细
	 * 1. 菜单列表时, 传入pid=0，只展示一级菜单列表
	 * 2. 展开菜单列表时， 传入pid=选择的菜单id的pid, 返回子节点，懒加载方式
	 *
	 * @return
	 */
	@GetMapping("tree-all")
	@SaCheckPermission("menu:list")
	public ResponseData<List<BuildMenuRouteNode>> allMenuTree() {
		return ResponseData.success(sysMenuService.allTree());
	}

    /**
     * 删除菜单以及遍历子节点
     *
     * @param request
     */
    @PostMapping("delete")
    @SaCheckPermission("menu:del")
    public ResponseData<Boolean> delete(@RequestBody BatchIdRequest request) {
        sysMenuService.delete(request.getIds());
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 获取部门同级别和上级节点数据
     *
     * @param query
     * @return
     */
    @GetMapping("superior")
    @SaCheckPermission("menu:list")
    public ResponseData<List<SysMenuNode>> fetchSameAndSuperiorData(SysMenuSuperiorQuery query) {
        return ResponseData.success(sysMenuService.fetchSameAndSuperiorData(query));
    }

    /**
     * 构建用户菜单树
     *
     * @return
     */
    @GetMapping("user-menu")
    public ResponseData<List<BuildMenuRouteNode>> buildUserMenuTree() {
        return ResponseData.success(sysMenuService.buildUserMenuTree(PrettyAdminSecurityUtils.getCurrentUserId()));
    }

    /**
     * 菜单同步
     *
     * @return
     */
    @PostMapping("sync")
    @SaCheckPermission("menu:sync")
    public ResponseData<Boolean> syncMenu() {
        final ScanPermissionPayload payload = permissionMenuScanner.scanPreAuthorizeMethods();
        sysMenuService.autoCreateMenu(payload);
        return ResponseData.success(Boolean.TRUE);
    }
}
