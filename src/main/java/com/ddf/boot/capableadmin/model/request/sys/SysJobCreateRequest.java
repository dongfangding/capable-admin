package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* <p>创建岗位</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/03 16:50
*/
@Data
public class SysJobCreateRequest {
    /**
     * ID, 新增无效， 编辑必传
     */
    private Long jobId;

    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    private String name;

    /**
     * 岗位状态
     */
    @NotNull(message = "岗位状态不能为空")
    private Boolean enabled;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    private Integer sort = 0;
}
