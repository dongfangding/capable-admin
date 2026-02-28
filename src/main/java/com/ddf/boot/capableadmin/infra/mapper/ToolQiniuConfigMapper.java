package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.ToolQiniuConfig;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface ToolQiniuConfigMapper {
    int deleteByPrimaryKey(Long configId);

    int insert(ToolQiniuConfig record);

    int insertSelective(ToolQiniuConfig record);

    ToolQiniuConfig selectByPrimaryKey(Long configId);

    int updateByPrimaryKeySelective(ToolQiniuConfig record);

    int updateByPrimaryKey(ToolQiniuConfig record);
}
