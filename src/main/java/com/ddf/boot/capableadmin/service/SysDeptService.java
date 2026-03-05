package com.ddf.boot.capableadmin.service;

import cn.hutool.core.collection.CollUtil;
import com.ddf.boot.capableadmin.infra.mapper.SysDeptMapper;
import com.ddf.boot.capableadmin.model.entity.SysDept;
import com.ddf.boot.capableadmin.model.entity.SysMenu;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptCreateRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptSuperiorQuery;
import com.ddf.boot.capableadmin.model.response.sys.MenuRouteNode;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptNode;
import com.ddf.boot.capableadmin.model.response.sys.SysDeptRes;
import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.api.util.DateUtils;
import com.ddf.boot.common.api.util.JsonUtil;
import com.ddf.boot.common.core.util.BeanCopierUtils;
import com.ddf.boot.common.core.util.PreconditionUtil;
import com.ddf.boot.common.core.util.TreeConvertUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 16:06
 */
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
@Service
public class SysDeptService {

    private final SysDeptMapper sysDeptMapper;


    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    public List<SysDeptNode> queryAll(SysDeptQuery query) {
        final List<SysDept> deptList = sysDeptMapper.queryAll(query);
		return buildDeptTree(deptList);
    }


	/**
	 * 构建用户菜单树
	 *
	 * @return
	 */
	public List<SysDeptNode> buildDeptTree(List<SysDept> deptList) {
		if (CollUtil.isEmpty(deptList)) {
			return Lists.newArrayList();
		}
		List<SysDeptNode> nodeList = new ArrayList<>();
		for (SysDept menu : deptList) {
			final SysDeptNode node = BeanCopierUtils.copy(menu,  SysDeptNode.class);
			nodeList.add(node);
		}
		return TreeConvertUtil.convert(nodeList);
	}

    /**
     * 持久化部门
     *
     * @param request
     * @return
     */
    public List<SysDeptNode> persist(SysDeptCreateRequest request) {
        final Long deptId = request.getDeptId();
        if (Objects.isNull(deptId)) {
            PreconditionUtil.checkArgument(
                    Objects.isNull(sysDeptMapper.findByName(request.getName())), "部门名称已存在");
            SysDept dept = BeanCopierUtils.copy(request, SysDept.class);
            final Long currentTimeSeconds = DateUtils.currentTimeSeconds();
            dept.setCreateTime(currentTimeSeconds);
            dept.setUpdateTime(currentTimeSeconds);
            sysDeptMapper.insertSelective(dept);
        } else {
            final SysDept oldDept = sysDeptMapper.selectByPrimaryKey(deptId);
            SysDept tmpDept;
            if (!oldDept.getName().equals(request.getName()) && Objects.nonNull(
                    tmpDept = sysDeptMapper.findByName(request.getName())) && !Objects.equals(request.getDeptId(), tmpDept.getDeptId())) {
                throw new BusinessException("部门名称已存在");
            }
            final Long newPid = request.getPid();
            final Long oldPid = oldDept.getPid();
            BeanCopierUtils.copy(request, oldDept);
			oldDept.setUpdateTime(DateUtils.currentTimeSeconds());
            sysDeptMapper.updateByPrimaryKeySelective(oldDept);

            // 更新自己作为别人的子节点时，其它数据的子节点数量
            if (Objects.nonNull(oldPid)) {
                if (!oldPid.equals(newPid)) {
                    updateSubCount(oldPid, true);
                }
            }
        }
        // 更新自己作为父节点时，自身下面有多少子节点
        updateSubCount(request.getDeptId(), true);
        return queryAll(new SysDeptQuery());
    }

    /**
     * 更新父节点下的子节点的数量
     *
     * @param deptId
     * @param clearCountIfZero
     * @return
     */
    public void updateSubCount(Long deptId, boolean clearCountIfZero) {
        final int count = sysDeptMapper.countByPid(deptId);
        if (clearCountIfZero) {
            sysDeptMapper.updateSubCount(deptId, count);
        } else if (count > 0) {
            sysDeptMapper.updateSubCount(deptId, count);
        }
    }

    /**
     * 获取部门同级别和上级节点数据
     *
     * @param query
     * @return
     */
    public List<SysDeptNode> fetchSameAndSuperiorData(SysDeptSuperiorQuery query) {
        final Long deptId = query.getDeptId();
        final boolean excludeSelfAndSub = query.isExcludeSelfAndSub();
        final SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (Objects.isNull(dept)) {
            return new ArrayList<>();
        }
        List<SysDept> superior = getSuperior(dept, new ArrayList<>());
        if (excludeSelfAndSub) {
            for (SysDept data : superior) {
                // 这种情况似乎不会出现？为了避免异常数据？
                if (data.getDeptId().equals(dept.getPid())) {
                    data.setSubCount(data.getSubCount() - 1);
                }
            }
            // 编辑部门时不显示自己以及自己下级的数据，避免出现PID数据环形问题(似乎只是为了过滤自己，下级数据问题在查询源头上已经避免了？)
            superior = superior.stream().filter(i -> !deptId.equals(i.getDeptId())).toList();
        }
        return buildTree(superior);
    }

    /**
     * 构建部门树结构
     *
     * @param deptList
     * @return
     */
    public List<SysDeptNode> buildTree(List<SysDept> deptList) {
        final List<SysDeptNode> originNodeList = BeanCopierUtils.copy(deptList, SysDeptNode.class);
        return TreeConvertUtil.convert(originNodeList);
    }

    /**
     * 获取当前节点同级别节点和上级节点，用于编辑节点时查看当前节点时使用
     *
     * @param dept
     * @param deptList
     * @return
     */
    public List<SysDept> getSuperior(SysDept dept, List<SysDept> deptList) {
        deptList.addAll(sysDeptMapper.findByPid(dept.getPid()));
        if (dept.getPid() == 0) {
            return deptList;
        }
        return getSuperior(sysDeptMapper.selectByPrimaryKey(dept.getPid()), deptList);
    }
}
