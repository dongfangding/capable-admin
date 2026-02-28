package com.ddf.boot.capableadmin.model.request.auth;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/03 17:51
 */
@Data
public class AdminLoginRequest implements Serializable {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码(临时关闭校验)
     */
    private String code;

    /**
     * uuid(临时关闭校验)
     */
    private String uuid;
}
