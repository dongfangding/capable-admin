package com.ddf.boot.capableadmin.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuListQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.MenuRouteNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuNode;
import com.ddf.boot.capableadmin.model.response.sys.SysMenuRes;
import com.ddf.boot.capableadmin.service.SysMenuService;
import com.ddf.boot.common.api.model.common.request.IdRequest;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.boot.common.mvc.permissionscan.PermissionMenuScanner;
import com.ddf.boot.common.mvc.permissionscan.ScanPermissionPayload;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统菜单管理控制器。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;
    private final PermissionMenuScanner permissionMenuScanner;

    /**
     * 新增或修改菜单。
     *
     * @param request 菜单保存请求
     * @return 是否保存成功
     */
    @PostMapping("persist")
    @SaCheckPermission(value = {"menu:add", "menu:edit"})
    @AdminAuditLog(module = "菜单管理", action = "保存菜单")
    public ResponseData<Boolean> persist(@RequestBody @Valid SysMenuCreateRequest request) {
        sysMenuService.persist(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 查询菜单列表。
     *
     * @param query 查询条件
     * @return 菜单列表
     */
    @GetMapping("list")
    @SaCheckPermission("menu:list")
    public ResponseData<List<SysMenuRes>> list(SysMenuListQuery query) {
        return ResponseData.success(sysMenuService.list(query));
    }

    /**
     * 查询完整菜单树。
     *
     * @return 菜单树
     */
    @GetMapping("tree-all")
    @SaCheckPermission("menu:list")
    public ResponseData<List<MenuRouteNode>> allMenuTree() {
        return ResponseData.success(sysMenuService.allTree());
    }

    /**
     * 删除菜单。
     *
     * @param request 菜单ID请求
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @SaCheckPermission("menu:del")
    @AdminAuditLog(module = "菜单管理", action = "删除菜单")
    public ResponseData<Boolean> delete(@RequestBody IdRequest request) {
        sysMenuService.delete(Set.of(request.getId()));
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 获取当前菜单的同级和上级节点。
     *
     * @param query 查询条件
     * @return 菜单节点列表
     */
    @GetMapping("superior")
    @SaCheckPermission("menu:list")
    public ResponseData<List<SysMenuNode>> fetchSameAndSuperiorData(SysMenuSuperiorQuery query) {
        return ResponseData.success(sysMenuService.fetchSameAndSuperiorData(query));
    }

    /**
     * 构建当前用户菜单树。
     *
     * @return 用户菜单树
     */
    @GetMapping("user-tree")
    public ResponseData<List<MenuRouteNode>> buildUserMenuTree() {
        return ResponseData.success(sysMenuService.buildUserMenuTree());
    }

    /**
     * 同步菜单权限定义。
     *
     * @return 是否同步成功
     */
    @PostMapping("sync")
    @SaCheckPermission("menu:sync")
    @AdminAuditLog(module = "菜单管理", action = "同步菜单")
    public ResponseData<Boolean> syncMenu() {
        ScanPermissionPayload payload = permissionMenuScanner.scanPreAuthorizeMethods();
        sysMenuService.autoCreateMenu(payload);
        return ResponseData.success(Boolean.TRUE);
    }
}
