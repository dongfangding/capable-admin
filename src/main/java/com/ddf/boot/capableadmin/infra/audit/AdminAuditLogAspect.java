package com.ddf.boot.capableadmin.infra.audit;

import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.entity.SysLog;
import com.ddf.boot.capableadmin.service.AdminAuditLogService;
import com.ddf.boot.common.api.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 后台操作审计切面。
 * 负责围绕带有 {@link AdminAuditLog} 注解的方法记录成功或失败日志。
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAuditLogAspect {

    private final AdminAuditLogService adminAuditLogService;

    /**
     * 记录被审计方法的执行日志。
     *
     * @param joinPoint      切点上下文
     * @param adminAuditLog  审计注解
     * @return 目标方法返回值
     * @throws Throwable 目标方法抛出的异常
     */
    @Around("@annotation(adminAuditLog)")
    public Object around(ProceedingJoinPoint joinPoint, AdminAuditLog adminAuditLog) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            adminAuditLogService.save(buildLog(joinPoint, adminAuditLog, start, null));
            return result;
        } catch (Throwable throwable) {
            adminAuditLogService.save(buildLog(joinPoint, adminAuditLog, start, throwable));
            throw throwable;
        }
    }

    /**
     * 根据执行上下文构建系统日志对象。
     *
     * @param joinPoint     切点上下文
     * @param adminAuditLog 审计注解
     * @param start         开始时间戳
     * @param throwable     异常对象，成功场景为 null
     * @return 待保存的系统日志
     */
    private SysLog buildLog(ProceedingJoinPoint joinPoint, AdminAuditLog adminAuditLog, long start, Throwable throwable) {
        SysLog log = new SysLog();
        log.setDescription(adminAuditLog.module() + ":" + adminAuditLog.action());
        log.setLogType(throwable == null ? "SUCCESS" : "FAIL");
        log.setMethod(((MethodSignature) joinPoint.getSignature()).getMethod().toGenericString());
        log.setParams(toJson(joinPoint.getArgs()));
        log.setTime(System.currentTimeMillis() - start);
        log.setUsername(resolveUsername());
        log.setCreateTime(Instant.now().toEpochMilli());

        HttpServletRequest request = currentRequest();
        if (request != null) {
            log.setRequestIp(request.getRemoteAddr());
            log.setBrowser(request.getHeader("User-Agent"));
        }
        if (throwable != null) {
            log.setExceptionDetail(throwable.getClass().getSimpleName() + ":" + throwable.getMessage());
        }
        return log;
    }

    /**
     * 获取当前操作用户名。
     *
     * @return 当前用户名，无法获取时降级为 anonymous
     */
    private String resolveUsername() {
        try {
            String username = PrettyAdminSecurityUtils.getCurrentUsername();
            return username == null || username.isBlank() ? "anonymous" : username;
        } catch (Exception ignored) {
            return "anonymous";
        }
    }

    /**
     * 将请求参数序列化为 JSON 文本。
     *
     * @param args 方法参数
     * @return 序列化结果
     */
    private String toJson(Object[] args) {
        try {
            return JsonUtil.toJson(args);
        } catch (Exception e) {
            return "<unserializable>";
        }
    }

    /**
     * 获取当前线程绑定的 HTTP 请求对象。
     *
     * @return 当前请求，若不存在则返回 null
     */
    private HttpServletRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }
}
