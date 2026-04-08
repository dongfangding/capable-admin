package com.ddf.boot.capableadmin.controller;

import com.ddf.boot.common.api.model.captcha.CaptchaType;
import com.ddf.boot.common.api.model.captcha.request.CaptchaCheckRequest;
import com.ddf.boot.common.api.model.captcha.request.CaptchaRequest;
import com.ddf.boot.common.api.model.captcha.response.ApplicationCaptchaResult;
import com.ddf.boot.common.api.model.captcha.response.CaptchaResult;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.common.captcha.helper.CaptchaHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 验证码控制器
 * </p>
 * 提供点选文字验证码的生成与校验接口
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/04/08
 */
@RestController
@RequestMapping("captcha")
@RequiredArgsConstructor
@Slf4j
public class CaptchaController {

    private final CaptchaHelper captchaHelper;

    /**
     * 生成点选文字验证码
     *
     * @return 验证码结果（底图base64、文字列表、uuid）
     */
    @GetMapping("generate")
    public ResponseData<ApplicationCaptchaResult> generate() {
        CaptchaRequest request = CaptchaRequest.builder()
                .captchaType(CaptchaType.CLICK_WORDS)
                .build();
        CaptchaResult result = captchaHelper.generate(request);
        log.debug("生成点选验证码, uuid: {}", result.getUuid());
        return ResponseData.success(ApplicationCaptchaResult.fromCaptchaResult(result));
    }

    /**
     * 校验点选文字验证码
     * 校验成功后验证码自动失效（防止重放攻击）
     *
     * @param request 校验请求（uuid、verifyCode、captchaType）
     * @return 校验结果
     */
    @PostMapping("check")
    public ResponseData<Boolean> check(@RequestBody @Valid CaptchaCheckRequest request) {
        log.debug("校验验证码, uuid: {}", request.getUuid());
        captchaHelper.check(request);
        return ResponseData.success(Boolean.TRUE);
    }
}
