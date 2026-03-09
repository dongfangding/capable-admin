package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.entity.SysUserDept;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface SysUserDeptMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUserDept record);

    int insertSelective(SysUserDept record);

    SysUserDept selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUserDept record);

    int updateByPrimaryKey(SysUserDept record);

    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("deptId") Long deptId);

    int deleteByDeptIds(@Param("deptIds") Set<Long> deptIds);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteByUserIds(@Param("userIds") Set<Long> userIds);

	int batchInsert(@Param("list") List<SysUserDept> list);

    List<Map<String, Object>> selectDeptIdsByUserIds(@Param("userIds") List<Long> userIds);
}
