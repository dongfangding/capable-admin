package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 当前用户修改个人资料请求。
 */
@Data
public class UserProfileUpdateRequest {

    /**
     * 用户昵称。
     */
    @NotBlank
    private String nickname;

    /**
     * 邮箱。
     */
    private String email;

    /**
     * 手机号。
     */
    private String mobile;

    /**
     * 头像地址。
     */
    private String avatar;

    /**
     * 性别。
     */
    @NotNull
    private Integer sex;
}
