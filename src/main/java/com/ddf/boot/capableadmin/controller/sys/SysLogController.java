package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysLogApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.SysLogListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysLogResponse;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
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
@RequestMapping("sys-log")
public class SysLogController {

    private final SysLogApplicationService sysLogApplicationService;

    /**
     * 分页查询系统日志。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @GetMapping("list")
    public ResponseData<PageResult<SysLogResponse>> list(@Valid SysLogListRequest request) {
        return ResponseData.success(sysLogApplicationService.list(request));
    }

    /**
     * 批量删除系统日志。
     *
     * @param request 日志ID集合
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @AdminAuditLog(module = "日志管理", action = "删除日志")
    public ResponseData<Boolean> delete(@RequestBody @Valid BatchIdRequest request) {
        sysLogApplicationService.delete(request.getIds());
        return ResponseData.success(Boolean.TRUE);
    }
}
