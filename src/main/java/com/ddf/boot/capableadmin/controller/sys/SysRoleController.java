package com.ddf.boot.capableadmin.controller.sys;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ddf.boot.capableadmin.model.request.sys.EnableRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleMenuUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysRoleRes;
import com.ddf.boot.capableadmin.service.SysRoleService;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
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
 * 系统角色管理
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/07 19:27
 */
@RequiredArgsConstructor
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
	@GetMapping("list")
	@SaCheckPermission("roles:list")
	public ResponseData<PageResult<SysRoleRes>> list(SysRoleListRequest request) {
		return ResponseData.success(sysRoleService.list(request));
	}


    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping("list-all")
    @SaCheckPermission("roles:list")
    public ResponseData<List<SysRoleRes>> listAll() {
        return ResponseData.success(sysRoleService.listAll());
    }

    /**
     * 保存角色
     *
     * @param request
     */
    @PostMapping("persist")
    @SaCheckPermission(value = { "roles:add", "roles:edit" })
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
    @SaCheckPermission("roles:edit")
    public ResponseData<Boolean> updateRoleMenu(@RequestBody @Valid SysRoleMenuUpdateRequest request) {
        sysRoleService.updateRoleMenu(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 更新启用状态
     *
     * @param request
     */
    @PostMapping("enable")
    @SaCheckPermission("roles:persist")
    public ResponseData<Boolean> enable(@RequestBody @Valid EnableRequest request) {
        return ResponseData.success(sysRoleService.updateEnable(request));
    }

	/**
	 * 删除角色
	 *
	 * @param request
	 */
	@PostMapping("delete")
	@SaCheckPermission("roles:del")
	public ResponseData<Boolean> delete(@RequestBody IdRequest request) {
		sysRoleService.delete(request.getId());
		return ResponseData.success(Boolean.TRUE);
	}
}
