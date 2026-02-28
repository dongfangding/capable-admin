package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.ToolQiniuContent;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface ToolQiniuContentMapper {
    int deleteByPrimaryKey(Long contentId);

    int insert(ToolQiniuContent record);

    int insertSelective(ToolQiniuContent record);

    ToolQiniuContent selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(ToolQiniuContent record);

    int updateByPrimaryKey(ToolQiniuContent record);
}
