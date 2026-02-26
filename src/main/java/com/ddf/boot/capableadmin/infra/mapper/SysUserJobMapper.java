package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUserJob;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/08 20:18
*/
public interface SysUserJobMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("jobId") Long jobId);

    int insert(SysUserJob record);

    int insertSelective(SysUserJob record);

    int batchInsertUserJob(@Param("userId") Long userId, @Param("jobIds") Set<Long> jobIds);

    void deleteByUserId(@Param("userId") Long userId);
}
