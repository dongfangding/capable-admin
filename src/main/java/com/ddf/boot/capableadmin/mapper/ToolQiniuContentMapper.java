package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.entity.ToolQiniuContent;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 16:50
 */
public interface ToolQiniuContentMapper {
    int deleteByPrimaryKey(Long contentId);

    int insert(ToolQiniuContent record);

    int insertSelective(ToolQiniuContent record);

    ToolQiniuContent selectByPrimaryKey(Long contentId);

    int updateByPrimaryKeySelective(ToolQiniuContent record);

    int updateByPrimaryKey(ToolQiniuContent record);
}
