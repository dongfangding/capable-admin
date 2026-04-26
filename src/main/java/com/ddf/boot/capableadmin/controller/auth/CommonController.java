package com.ddf.boot.capableadmin.controller.auth;

import com.ddf.boot.capableadmin.model.response.CaptchaCheckResponse;
import com.ddf.boot.common.api.model.captcha.CaptchaType;
import com.ddf.boot.common.api.model.captcha.request.CaptchaCheckRequest;
import com.ddf.boot.common.api.model.captcha.request.CaptchaRequest;
import com.ddf.boot.common.api.model.captcha.response.ApplicationCaptchaResult;
import com.ddf.boot.common.api.model.captcha.response.CaptchaCheckResult;
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
 * <p>通用控制器</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/04/25 22:38
 */
@RestController
@RequestMapping("common")
@RequiredArgsConstructor
@Slf4j
public class CommonController {

	private final CaptchaHelper captchaHelper;

	/**
	 * 生成点选验证码
	 *
	 * @return 验证码结果（底图 base64、文字列表、uuid）
	 */
	@GetMapping("captcha/generate")
	public ResponseData<ApplicationCaptchaResult> generate() {
		CaptchaRequest request = CaptchaRequest.builder()
				.captchaType(CaptchaType.CLICK_WORDS)
				.build();
		CaptchaResult result = captchaHelper.generate(request);
		return ResponseData.success(ApplicationCaptchaResult.fromCaptchaResult(result));
	}

	/**
	 * 校验点选验证码（一次校验）
	 * 校验成功后返回二次校验凭证 captchaVerification
	 *
	 * @param request 校验请求（uuid、verifyCode、captchaType、captchaVerification）
	 * @return 校验结果及 captchaVerification
	 */
	@PostMapping("captcha/check")
	public ResponseData<CaptchaCheckResult> check(@RequestBody @Valid CaptchaCheckRequest request) {
		return ResponseData.success(captchaHelper.check(request));
	}
}
