package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 当前用户修改密码请求。
 */
@Data
public class UserChangePasswordRequest {

    /**
     * 旧密码。
     */
    @NotBlank
    private String oldPassword;

    /**
     * 新密码。
     */
    @NotBlank
    private String newPassword;
}
