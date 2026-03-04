package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptNode;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptRes;
import com.ddf.boot.capableadmin.service.SysDeptService;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理
 *
 * @author snowball
 * @since 2025/1/4 下午2:55
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-dept")
public class SysDeptController {

    private final SysDeptService deptService;
    private final SysUserApplicationService sysUserApplicationService;

    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    @PostMapping("query-all")
    public ResponseData<List<SysDeptRes>> queryAll(@RequestBody @Valid SysDeptQuery query) {
        return ResponseData.success(deptService.queryAll(query));
    }

    /**
     * 持久化部门
     *
     * @param request
     * @return
     */
    @PostMapping("persist")
    public ResponseData<List<SysDeptRes>> persist(@RequestBody SysDeptCreateRequest request) {
        return ResponseData.success(deptService.persist(request));
    }

    /**
     * 获取部门同级别和上级节点数据
     *
     * @param query
     * @return
     */
    @PostMapping("superior")
    public ResponseData<List<SysDeptNode>> fetchSameAndSuperiorData(@RequestBody @Valid SysDeptSuperiorQuery query) {
        return ResponseData.success(deptService.fetchSameAndSuperiorData(query));
    }

    /**
     * 删除部门，当解散做，不会校验部门下是否有用户，如果有用户的话，删除用户与部门关联
     *
     * @param request
     * @return
     */
    @PostMapping("delete")
    public ResponseData<List<SysDeptRes>> deleteDept(@RequestBody @Valid BatchIdRequest request) {
        return ResponseData.success(sysUserApplicationService.deleteDept(request.getIds()));
    }
}
