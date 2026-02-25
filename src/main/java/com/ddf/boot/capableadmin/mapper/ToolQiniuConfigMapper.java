package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.entity.ToolQiniuConfig;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 16:50
 */
public interface ToolQiniuConfigMapper {
    int deleteByPrimaryKey(Long configId);

    int insert(ToolQiniuConfig record);

    int insertSelective(ToolQiniuConfig record);

    ToolQiniuConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(ToolQiniuConfig record);

    int updateByPrimaryKey(ToolQiniuConfig record);
}
