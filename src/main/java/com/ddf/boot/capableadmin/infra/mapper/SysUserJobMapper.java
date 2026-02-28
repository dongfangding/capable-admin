package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUserJob;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface SysUserJobMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("jobId") Long jobId);

    int insert(SysUserJob record);

    int insertSelective(SysUserJob record);

    int batchInsertUserJob(@Param("userId") Long userId, @Param("jobIds") Set<Long> jobIds);

    void deleteByUserId(@Param("userId") Long userId);
}
