package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysDictApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictDetailResponse;
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
@RequestMapping("sys-dict-detail")
public class SysDictDetailController {

    private final SysDictApplicationService sysDictApplicationService;

    /**
     * 分页查询字典明细。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    @GetMapping("list")
    public ResponseData<PageResult<SysDictDetailResponse>> list(@Valid SysDictDetailListRequest request) {
        return ResponseData.success(sysDictApplicationService.listDictDetail(request));
    }

    /**
     * 新增或修改字典明细。
     *
     * @param request 保存请求
     * @return 是否保存成功
     */
    @PostMapping("persist")
    @AdminAuditLog(module = "字典明细管理", action = "保存字典明细")
    public ResponseData<Boolean> persist(@RequestBody @Valid SysDictDetailPersistRequest request) {
        sysDictApplicationService.persistDictDetail(request);
        return ResponseData.success(Boolean.TRUE);
    }

    /**
     * 删除字典明细。
     *
     * @param request 明细ID
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @AdminAuditLog(module = "字典明细管理", action = "删除字典明细")
    public ResponseData<Boolean> delete(@RequestBody @Valid IdRequest request) {
        sysDictApplicationService.deleteDictDetail(request.getId());
        return ResponseData.success(Boolean.TRUE);
    }
}
