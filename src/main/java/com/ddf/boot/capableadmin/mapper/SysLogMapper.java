package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.entity.SysLog;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 16:50
 */
public interface SysLogMapper {
    int deleteByPrimaryKey(Long logId);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    SysLog selectByPrimaryKey(Long logId);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKey(SysLog record);
}
