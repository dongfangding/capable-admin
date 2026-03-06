package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

/**
* <p>系统用户</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/07 20:33
*/
@Data
public class SysUserCreateRequest {
    /**
     * ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别0 女 1 男
     */
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 状态：1启用、0禁用
     */
    @NotNull(message = "状态不能为空")
    private Boolean enabled;

    /**
     * 部门名称
     */
    @NotNull(message = "部门不能为空")
    private Set<Long> deptIds;

    /**
     * 角色id集合
     */
    @NotEmpty(message = "角色不能为空")
    private Set<Long> roleIds;

	private Set<Long> jobIds;
}
