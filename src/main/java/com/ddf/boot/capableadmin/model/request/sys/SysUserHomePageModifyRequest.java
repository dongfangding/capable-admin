package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
* <p>用户主页信息修改</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/07 20:33
*/
@Data
public class SysUserHomePageModifyRequest {
    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    /**
     * 性别0 女 1 男
     */
    @NotBlank(message = "性别不能为空")
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
}
