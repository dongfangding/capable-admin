package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.SysJobCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysJobQuery;
import com.ddf.boot.capableadmin.model.response.sys.SysJobRes;
import com.ddf.boot.capableadmin.service.SysJobService;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 岗位管理控制器。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-job")
public class SysJobController {

    private final SysJobService sysJobService;

    /**
     * 分页查询岗位列表。
     *
     * @param query 查询条件
     * @return 岗位分页结果
     */
    @GetMapping("query")
    public ResponseData<PageResult<SysJobRes>> queryJob(SysJobQuery query) {
        return ResponseData.success(sysJobService.query(query));
    }

    /**
     * 新增或修改岗位。
     *
     * @param request 岗位保存请求
     * @return 是否保存成功
     */
    @PostMapping("persist")
    @AdminAuditLog(module = "岗位管理", action = "保存岗位")
    public ResponseData<Boolean> persist(@Validated @RequestBody SysJobCreateRequest request) {
        return ResponseData.success(sysJobService.persist(request) > 0);
    }

    /**
     * 批量删除岗位。
     *
     * @param ids 岗位ID集合
     * @return 是否删除成功
     */
    @PostMapping("delete")
    @AdminAuditLog(module = "岗位管理", action = "删除岗位")
    public ResponseData<Boolean> deleteJob(@RequestBody Set<Long> ids) {
        return ResponseData.success(sysJobService.deleteJob(ids) > 0);
    }
}
