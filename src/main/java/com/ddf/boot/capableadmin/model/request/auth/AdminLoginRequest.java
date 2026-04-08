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
     * 验证码答案（点选坐标JSON数组序列化字符串）
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 验证码唯一标识
     */
    @NotBlank(message = "验证码ID不能为空")
    private String uuid;
}
