package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptNode;
import com.ddf.boot.capableadmin.service.SysDeptService;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门管理控制器。
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-dept")
public class SysDeptController {

    private final SysDeptService deptService;
    private final SysUserApplicationService sysUserApplicationService;

    /**
     * 查询部门列表。
     *
     * @param query 查询条件
     * @return 部门树列表
     */
    @GetMapping("list")
    public ResponseData<List<SysDeptNode>> queryAll(@Valid SysDeptQuery query) {
        return ResponseData.success(deptService.queryAll(query));
    }

    /**
     * 新增或修改部门。
     *
     * @param request 部门保存请求
     * @return 最新部门树
     */
    @PostMapping("persist")
    @AdminAuditLog(module = "部门管理", action = "保存部门")
    public ResponseData<List<SysDeptNode>> persist(@RequestBody SysDeptCreateRequest request) {
        return ResponseData.success(deptService.persist(request));
    }

    /**
     * 获取部门的同级和上级节点。
     *
     * @param query 查询条件
     * @return 部门节点列表
     */
    @PostMapping("superior")
    public ResponseData<List<SysDeptNode>> fetchSameAndSuperiorData(@RequestBody @Valid SysDeptSuperiorQuery query) {
        return ResponseData.success(deptService.fetchSameAndSuperiorData(query));
    }

    /**
     * 删除部门，同时解除用户与部门的关联关系。
     *
     * @param request 部门ID集合
     * @return 最新部门树
     */
    @PostMapping("delete")
    @AdminAuditLog(module = "部门管理", action = "删除部门")
    public ResponseData<List<SysDeptNode>> deleteDept(@RequestBody @Valid BatchIdRequest request) {
        return ResponseData.success(sysUserApplicationService.deleteDept(request.getIds()));
    }
}
