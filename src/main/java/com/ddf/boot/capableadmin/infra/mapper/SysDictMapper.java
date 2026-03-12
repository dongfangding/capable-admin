package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysDict;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/03/12 17:50
 */
public interface SysDictMapper {
    int deleteByPrimaryKey(Long dictId);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Long dictId);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);
}