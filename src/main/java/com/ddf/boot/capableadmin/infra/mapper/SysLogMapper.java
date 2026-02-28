package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysLog;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface SysLogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
}
