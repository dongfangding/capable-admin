package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.boot.capableadmin.config.annotation.RequirePermission;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleMenuUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysRoleRes;
import com.ddf.boot.capableadmin.service.SysRoleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统角色管理
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/07 19:27
 */
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
@Slf4j
@RestController
@RequestMapping("sys-role")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping("list-all")
    @RequirePermission("roles:list")
    public ResponseData<List<SysRoleRes>> listAll() {
        return ResponseData.success(sysRoleService.listAll());
    }

    /**
     * 保存角色
     *
     * @param request
     */
    @PostMapping("persist")
    @RequirePermission(value = { "roles:add", "roles:edit" })
    public ResponseData<Boolean> persist(@RequestBody @Valid SysRoleCreateRequest request) {
        sysRoleService.persist(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 更新角色菜单
     *
     * @param request
     */
    @PostMapping("update-role-menu")
    @RequirePermission("roles:edit")
    public ResponseData<Boolean> updateRoleMenu(@RequestBody @Valid SysRoleMenuUpdateRequest request) {
        sysRoleService.updateRoleMenu(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 删除角色
     *
     * @param request
     */
    @PostMapping("delete")
    @RequirePermission("roles:del")
    public ResponseData<Boolean> delete(@RequestBody BatchIdRequest request) {
        sysRoleService.delete(request.getIds());
        return ResponseData.success(Boolean.TRUE);
    }
}
