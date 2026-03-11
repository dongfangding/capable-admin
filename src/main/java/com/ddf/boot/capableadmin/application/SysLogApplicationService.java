package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.infra.mapper.ext.SysLogExtMapper;
import com.ddf.boot.capableadmin.model.request.sys.SysLogListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysLogResponse;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PageUtil;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 系统日志应用服务。
 */
@Service
@RequiredArgsConstructor
public class SysLogApplicationService {

    private final SysLogExtMapper sysLogExtMapper;

    /**
     * 分页查询系统日志。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    public PageResult<SysLogResponse> list(SysLogListRequest request) {
        return PageUtil.startPage(
                request,
                () -> sysLogExtMapper.listByCondition(request),
                list -> BeanCopierUtils.copy(list, SysLogResponse.class)
        );
    }

    /**
     * 批量删除系统日志。
     *
     * @param ids 日志ID集合
     */
    public void delete(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        sysLogExtMapper.deleteByIds(ids);
    }
}
