package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
* <p>创建角色</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/07 17:55
*/
@Data
public class SysRoleCreateRequest {
    /**
     * ID
     */
    private Long roleId;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;

    /**
     * 角色级别
     */
    private Integer level = 0;

    /**
     * 描述
     */
    private String description;

    /**
     * ip白名单（指定IP登入）
     */
    private String ipLimit;

    /**
     * 排序
     */
    private Integer sort = 0;

	/**
	 * 是否启用， 0否1是
	 */
	private Boolean enable;
}
