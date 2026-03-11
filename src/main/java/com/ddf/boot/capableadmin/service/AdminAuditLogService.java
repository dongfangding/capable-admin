package com.ddf.boot.capableadmin.service;

import com.ddf.boot.capableadmin.infra.mapper.SysLogMapper;
import com.ddf.boot.capableadmin.model.entity.SysLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 后台操作审计日志服务。
 * 负责对参数进行脱敏和截断后写入 sys_log。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuditLogService {

    private static final int MAX_PARAMS_LENGTH = 2048;

    private final SysLogMapper sysLogMapper;

    /**
     * 保存操作审计日志。
     *
     * @param sysLog 待保存的日志对象
     */
    public void save(SysLog sysLog) {
        try {
            sysLog.setParams(sanitizeParams(sysLog.getParams()));
            sysLogMapper.insertSelective(sysLog);
        } catch (Exception e) {
            log.error("save audit log failed", e);
        }
    }

    /**
     * 对日志参数进行脱敏和长度控制。
     *
     * @param params 原始参数文本
     * @return 处理后的参数文本
     */
    String sanitizeParams(String params) {
        if (params == null || params.isBlank()) {
            return params;
        }
        String sanitized = params
                .replaceAll("(?i)(\"(?:oldPassword|newPassword|password|token)\"\\s*:\\s*\")([^\"]*)(\")", "$1***$3");
        if (sanitized.length() > MAX_PARAMS_LENGTH) {
            return sanitized.substring(0, MAX_PARAMS_LENGTH);
        }
        return sanitized;
    }
}
