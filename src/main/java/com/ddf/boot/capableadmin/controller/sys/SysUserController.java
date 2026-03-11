package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.EnableRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysUserCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysUserListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysUserRes;
import com.ddf.boot.common.api.model.common.request.IdRequest;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户管理控制器。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-user")
public class SysUserController {

    private final SysUserApplicationService userApplicationService;

    /**
     * 分页查询用户列表。
     *
     * @param request 查询条件
     * @return 用户分页结果
     */
    @GetMapping("list")
    public ResponseData<PageResult<SysUserRes>> list(@Valid SysUserListRequest request) {
        return ResponseData.success(userApplicationService.list(request));
    }

    /**
     * 新增或修改用户。
     *
     * @param request 用户保存请求
     * @return 是否保存成功
     */
    @PostMapping("persist")
    @AdminAuditLog(module = "用户管理", action = "保存用户")
    public ResponseData<Boolean> persist(@RequestBody @Valid SysUserCreateRequest request) {
        userApplicationService.persistUser(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 重置指定用户密码。
     *
     * @param request 用户ID请求
     * @return 是否重置成功
     */
    @PostMapping("reset-password")
    @AdminAuditLog(module = "用户管理", action = "重置密码")
    public ResponseData<Boolean> resetPassword(@RequestBody @Valid IdRequest request) {
        userApplicationService.resetPassword(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 删除指定用户。
     *
     * @param request 用户ID请求
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @AdminAuditLog(module = "用户管理", action = "删除用户")
    public ResponseData<Boolean> delete(@RequestBody @Valid IdRequest request) {
        userApplicationService.delete(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 修改用户启用状态。
     *
     * @param request 启停请求
     * @return 是否修改成功
     */
    @PostMapping("enable")
    @AdminAuditLog(module = "用户管理", action = "修改启用状态")
    public ResponseData<Boolean> enable(@RequestBody @Valid EnableRequest request) {
        return ResponseData.success(userApplicationService.updateEnable(request));
    }
}
