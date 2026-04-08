package com.ddf.boot.capableadmin.model.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 验证码校验响应
 * </p>
 * 包含一次校验成功后返回的二次校验凭证 captchaVerification
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/04/08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaCheckResponse implements Serializable {

    /**
     * 二次校验凭证，用于登录等业务接口的二次验证
     */
    private String captchaVerification;
}
