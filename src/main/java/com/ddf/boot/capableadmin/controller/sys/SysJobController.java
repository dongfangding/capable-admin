/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ddf.boot.capableadmin.controller.sys;

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
 * 岗位
 *
 * @author snowball
 * @since 2025/1/3 下午5:15
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("sys-job")
public class SysJobController {

    private final SysJobService sysJobService;

    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    @GetMapping("query")
    public ResponseData<PageResult<SysJobRes>> queryJob(SysJobQuery query) {
        return ResponseData.success(sysJobService.query(query));
    }

    /**
     * 持久化岗位
     *
     * @param request
     * @return
     */
    @PostMapping("persist")
    public ResponseData<Boolean> persist(@Validated @RequestBody SysJobCreateRequest request) {
        return ResponseData.success(sysJobService.persist(request) > 0);
    }

    /**
     * 删除岗位
     *
     * @param ids
     * @return
     */
    @PostMapping("delete")
    public ResponseData<Boolean> deleteJob(@RequestBody Set<Long> ids) {
        return ResponseData.success(sysJobService.deleteJob(ids) > 0);
    }
}
