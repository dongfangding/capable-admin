package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/04 16:43
 */
@Data
public class SysDeptCreateRequest implements Serializable {

    /**
     * ID
     */
    private Long deptId;

    /**
     * 上级部门
     */
    private Long pid = 0L;

    /**
     * 名称
     */
    @NotBlank(message = "name不能为空")
    private String name;

    /**
     * 排序
     */
    private Integer sort = 0;

    /**
     * 状态, 0 禁用 1 启用
     */
    private Boolean enabled = Boolean.TRUE;
}
