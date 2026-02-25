package com.ddf.boot.capableadmin.service;

import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.api.util.DateUtils;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PageUtil;
import com.ddf.boot.common.core.util.PreconditionUtil;
import com.ddf.boot.capableadmin.mapper.SysJobMapper;
import com.ddf.boot.capableadmin.model.entity.SysJob;
import com.ddf.boot.capableadmin.model.request.sys.SysJobCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysJobQuery;
import com.ddf.boot.capableadmin.model.response.sys.SysJobRes;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 16:55
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Service
public class SysJobService {

    private final SysJobMapper sysJobMapper;


    /**
     * 查询岗位
     *
     * @param query
     * @return
     */
    public PageResult<SysJobRes> query(SysJobQuery query) {
        return PageUtil.startPage(
                query, () -> sysJobMapper.query(query), (list) -> BeanCopierUtils.copy(list, SysJobRes.class));
    }


    /**
     * 保存岗位
     *
     * @param request
     * @return
     */
    public int persist(SysJobCreateRequest request) {
        final Long jobId = request.getJobId();
        if (Objects.isNull(jobId)) {
            PreconditionUtil.checkArgument(
                    Objects.isNull(sysJobMapper.findByName(request.getName())), "岗位名称已存在");
            SysJob job = BeanCopierUtils.copy(request, SysJob.class);
            final Long currentTimeSeconds = DateUtils.currentTimeSeconds();
            job.setCreateTime(currentTimeSeconds);
            job.setUpdateTime(currentTimeSeconds);
            return sysJobMapper.insertSelective(job);
        } else {
            final SysJob job = sysJobMapper.selectByPrimaryKey(jobId);
            if (Objects.isNull(job)) {
                throw new BusinessException("岗位不存在");
            }
            SysJob tmpJob;
            if (!job.getName().equals(request.getName()) && Objects.nonNull(
                    tmpJob = sysJobMapper.findByName(request.getName())) && !Objects.equals(request.getJobId(), tmpJob.getJobId())) {
                throw new BusinessException("岗位名称已存在");
            }
            job.setUpdateTime(DateUtils.currentTimeSeconds());
            return sysJobMapper.updateByPrimaryKeySelective(job);
        }
    }

    /**
     * 删除岗位
     *
     * @param ids
     */
    public int deleteJob(Set<Long> ids) {
        if (sysJobMapper.countByJobs(ids) > 0) {
            throw new BusinessException("所选的岗位中存在用户关联，请解除关联再试！");
        }
        return sysJobMapper.deleteBatchIds(ids);
    }

}
