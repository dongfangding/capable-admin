package com.ddf.boot.capableadmin.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.EnableRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleMenuUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysRoleRes;
import com.ddf.boot.capableadmin.service.SysRoleService;
import com.ddf.boot.common.api.model.common.request.IdRequest;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色管理控制器。
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("sys-role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 分页查询角色列表。
     *
     * @param request 查询条件
     * @return 角色分页结果
     */
    @GetMapping("list")
    @SaCheckPermission("roles:list")
    public ResponseData<PageResult<SysRoleRes>> list(SysRoleListRequest request) {
        return ResponseData.success(sysRoleService.list(request));
    }

    /**
     * 查询全部角色。
     *
     * @return 角色列表
     */
    @GetMapping("list-all")
    @SaCheckPermission("roles:list")
    public ResponseData<List<SysRoleRes>> listAll() {
        return ResponseData.success(sysRoleService.listAll());
    }

    /**
     * 新增或修改角色。
     *
     * @param request 角色保存请求
     * @return 是否保存成功
     */
    @PostMapping("persist")
    @SaCheckPermission(value = {"roles:add", "roles:edit"})
    @AdminAuditLog(module = "角色管理", action = "保存角色")
    public ResponseData<Boolean> persist(@RequestBody @Valid SysRoleCreateRequest request) {
        sysRoleService.persist(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 更新角色菜单授权。
     *
     * @param request 角色菜单更新请求
     * @return 是否更新成功
     */
    @PostMapping("update-role-menu")
    @SaCheckPermission("roles:edit")
    @AdminAuditLog(module = "角色管理", action = "更新角色菜单")
    public ResponseData<Boolean> updateRoleMenu(@RequestBody @Valid SysRoleMenuUpdateRequest request) {
        sysRoleService.updateRoleMenu(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 修改角色启用状态。
     *
     * @param request 启停请求
     * @return 是否修改成功
     */
    @PostMapping("enable")
    @SaCheckPermission("roles:persist")
    @AdminAuditLog(module = "角色管理", action = "修改启用状态")
    public ResponseData<Boolean> enable(@RequestBody @Valid EnableRequest request) {
        return ResponseData.success(sysRoleService.updateEnable(request));
    }

    /**
     * 删除角色。
     *
     * @param request 角色ID请求
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @SaCheckPermission("roles:del")
    @AdminAuditLog(module = "角色管理", action = "删除角色")
    public ResponseData<Boolean> delete(@RequestBody IdRequest request) {
        sysRoleService.delete(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }
}
