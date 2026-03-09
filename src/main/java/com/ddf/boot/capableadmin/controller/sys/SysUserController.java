package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("sys-user")
public class SysUserController {

    private final SysUserApplicationService userApplicationService;

    @GetMapping("list")
    public ResponseData<PageResult<SysUserRes>> list(@Valid SysUserListRequest request) {
        return ResponseData.success(userApplicationService.list(request));
    }

    @PostMapping("persist")
    public ResponseData<Boolean> persist(@RequestBody @Valid SysUserCreateRequest request) {
        userApplicationService.persistUser(request);
        return ResponseData.success(Boolean.TRUE);
    }

    @PostMapping("reset-password")
    public ResponseData<Boolean> resetPassword(@RequestBody @Valid IdRequest request) {
        userApplicationService.resetPassword(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }

    @PostMapping("delete")
    public ResponseData<Boolean> delete(@RequestBody @Valid IdRequest request) {
        userApplicationService.delete(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }

    @PostMapping("enable")
    public ResponseData<Boolean> enable(@RequestBody @Valid EnableRequest request) {
        return ResponseData.success(userApplicationService.updateEnable(request));
    }
}
