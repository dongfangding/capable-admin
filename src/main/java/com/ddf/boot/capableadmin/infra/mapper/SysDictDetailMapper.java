package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysDictDetail;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface SysDictDetailMapper {
    int deleteByPrimaryKey(Long detailId);

    int insert(SysDictDetail record);

    int insertSelective(SysDictDetail record);

    SysDictDetail selectByPrimaryKey(Long detailId);

    int updateByPrimaryKeySelective(SysDictDetail record);

    int updateByPrimaryKey(SysDictDetail record);
}
