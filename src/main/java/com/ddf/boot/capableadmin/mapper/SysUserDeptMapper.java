package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUserDept;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/07 20:54
*/
public interface SysUserDeptMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("deptId") Long deptId);

    int insert(SysUserDept record);

    int insertSelective(SysUserDept record);

    int deleteByDeptIds(@Param("deptIds")Set<Long> deptIds);
}
