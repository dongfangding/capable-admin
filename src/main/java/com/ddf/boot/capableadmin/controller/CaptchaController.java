package com.ddf.boot.capableadmin.controller;

import com.ddf.boot.capableadmin.model.response.CaptchaCheckResponse;
import com.ddf.boot.common.api.model.captcha.CaptchaType;
import com.ddf.boot.common.api.model.captcha.request.CaptchaCheckRequest;
import com.ddf.boot.common.api.model.captcha.request.CaptchaRequest;
import com.ddf.boot.common.api.model.captcha.response.ApplicationCaptchaResult;
import com.ddf.boot.common.api.model.captcha.response.CaptchaResult;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.common.captcha.helper.CaptchaHelper;
import com.anji.captcha.model.common.CaptchaTypeEnum;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final CaptchaService captchaService;
    private final ObjectMapper objectMapper;

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
     * 校验点选文字验证码（一次校验）
     * 校验成功后返回二次校验凭证 captchaVerification，前端需保存
     * 用于后续业务接口的二次验证（如登录）
     *
     * @param request 校验请求（uuid、verifyCode、captchaType）
     * @return 校验结果及 captchaVerification
     */
    @PostMapping("check")
    public ResponseData<CaptchaCheckResponse> check(@RequestBody @Valid CaptchaCheckRequest request) {
        log.debug("校验验证码, uuid: {}", request.getUuid());
        // 构建 SDK 校验请求
        CaptchaVO vo = new CaptchaVO();
        vo.setToken(request.getUuid());
        vo.setPointJson(request.getVerifyCode());
        vo.setCaptchaType(CaptchaTypeEnum.CLICKWORD.getCodeValue());
        vo.setCaptchaVerification(request.getCaptchaVerification());
        // 一次性校验，SDK 内部会验证坐标并生成 captchaVerification
        ResponseModel checkResult = captchaService.check(vo);
        if (!"0000".equals(checkResult.getRepCode())) {
            throw new com.ddf.boot.common.api.exception.BusinessException(
                    checkResult.getRepCode(), checkResult.getRepMsg());
        }
        // 从响应数据中提取 captchaVerification
        String captchaVerification = extractCaptchaVerification(checkResult);
        CaptchaCheckResponse response = new CaptchaCheckResponse();
        response.setCaptchaVerification(captchaVerification);
        return ResponseData.success(response);
    }

    /**
     * 从 SDK 响应中提取 captchaVerification
     *
     * @param model SDK 响应
     * @return captchaVerification 字符串
     */
    @SuppressWarnings("unchecked")
    private String extractCaptchaVerification(ResponseModel model) {
        try {
            if (model.getRepData() != null) {
                String json = objectMapper.writeValueAsString(model.getRepData());
                var node = objectMapper.readTree(json);
                return node.has("captchaVerification") ? node.get("captchaVerification").asText() : "";
            }
        } catch (Exception e) {
            log.warn("提取 captchaVerification 失败: {}", e.getMessage());
        }
        return "";
    }
}
