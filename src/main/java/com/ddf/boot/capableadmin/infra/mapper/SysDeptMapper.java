package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysDept;
import com.ddf.boot.capableadmin.model.request.sys.SysDeptQuery;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/27 17:52
 */
public interface SysDeptMapper {
    int deleteByPrimaryKey(Long deptId);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Long deptId);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> queryAll(SysDeptQuery query);

    /**
     * 查询子节点数量
     *
     * @param pid
     * @return
     */
    int countByPid(@Param("pid") Long pid);

    /**
     * 更新部门子节点数量
     *
     * @param deptId
     * @param subCount
     * @return
     */
    int updateSubCount(@Param("deptId") Long deptId, @Param("subCount") int subCount);

    /**
     * 批量删除
     *
     * @param deptIds
     * @return
     */
    int deleteByDeptIds(@Param("deptIds") Set<Long> deptIds);

    /**
     * 根据部门id批量查询
     *
     * @param deptIds
     * @return
     */
    List<SysDept> selectByDeptIds(@Param("deptIds") Set<Long> deptIds);

    /**
     * 根据pid查询部门列表
     *
     * @param pid
     * @return
     */
    List<SysDept> findByPid(@Param("pid") Long pid);

    SysDept findByName(@Param("name") String name);
}