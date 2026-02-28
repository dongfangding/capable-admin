package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/03 17:05
 */
@Data
public class SysJobQuery implements PageRequest {

    private Integer pageNum;
    private Integer pageSize;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 状态
     */
    private Boolean enabled;
}
