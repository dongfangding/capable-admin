package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.infra.mapper.SysDictDetailMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysDictMapper;
import com.ddf.boot.capableadmin.infra.mapper.ext.SysDictDetailExtMapper;
import com.ddf.boot.capableadmin.infra.mapper.ext.SysDictExtMapper;
import com.ddf.boot.capableadmin.model.entity.SysDict;
import com.ddf.boot.capableadmin.model.entity.SysDictDetail;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictPersistRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictDetailResponse;
import com.ddf.boot.capableadmin.model.response.sys.SysDictResponse;
import com.ddf.boot.common.api.model.common.response.PageResult;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字典与字典明细应用服务。
 */
@Service
@RequiredArgsConstructor
public class SysDictApplicationService {

    private final SysDictMapper sysDictMapper;
    private final SysDictDetailMapper sysDictDetailMapper;
    private final SysDictExtMapper sysDictExtMapper;
    private final SysDictDetailExtMapper sysDictDetailExtMapper;

    /**
     * 分页查询字典。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    public PageResult<SysDictResponse> listDict(SysDictListRequest request) {
        return PageUtil.startPage(
                request,
                () -> sysDictExtMapper.listByCondition(request),
                list -> BeanCopierUtils.copy(list, SysDictResponse.class)
        );
    }

    /**
     * 新增或修改字典。
     *
     * @param request 保存请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void persistDict(SysDictPersistRequest request) {
        SysDict sysDict = BeanCopierUtils.copy(request, SysDict.class);
        if (request.getDictId() == null) {
            sysDictMapper.insertSelective(sysDict);
            return;
        }
        sysDictMapper.updateByPrimaryKeySelective(sysDict);
    }

    /**
     * 删除字典，同时删除其下明细。
     *
     * @param dictId 字典ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long dictId) {
        sysDictDetailExtMapper.deleteByDictId(dictId);
        sysDictMapper.deleteByPrimaryKey(dictId);
    }

    /**
     * 分页查询字典明细。
     *
     * @param request 查询条件
     * @return 分页结果
     */
    public PageResult<SysDictDetailResponse> listDictDetail(SysDictDetailListRequest request) {
        return PageUtil.startPage(
                request,
                () -> sysDictDetailExtMapper.listByCondition(request),
                list -> BeanCopierUtils.copy(list, SysDictDetailResponse.class)
        );
    }

    /**
     * 新增或修改字典明细。
     *
     * @param request 保存请求
     */
    @Transactional(rollbackFor = Exception.class)
    public void persistDictDetail(SysDictDetailPersistRequest request) {
        SysDictDetail detail = BeanCopierUtils.copy(request, SysDictDetail.class);
        if (request.getDetailId() == null) {
            sysDictDetailMapper.insertSelective(detail);
            return;
        }
        sysDictDetailMapper.updateByPrimaryKeySelective(detail);
    }

    /**
     * 删除字典明细。
     *
     * @param detailId 明细ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictDetail(Long detailId) {
        sysDictDetailMapper.deleteByPrimaryKey(detailId);
    }
}
