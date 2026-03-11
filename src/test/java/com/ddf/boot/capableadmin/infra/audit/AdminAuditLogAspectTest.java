package com.ddf.boot.capableadmin.infra.audit;

import com.ddf.boot.capableadmin.model.entity.SysLog;
import com.ddf.boot.capableadmin.service.AdminAuditLogService;
import java.lang.reflect.Method;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class AdminAuditLogAspectTest {

    @Test
    void shouldWriteSuccessLogForAnnotatedMethod() throws Throwable {
        AdminAuditLogService auditLogService = Mockito.mock(AdminAuditLogService.class);
        AdminAuditLogAspect aspect = new AdminAuditLogAspect(auditLogService);
        ProceedingJoinPoint joinPoint = mockJoinPoint("ok");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/sys-user/reset-password");
        request.addHeader("User-Agent", "JUnit");
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Object result = aspect.around(joinPoint, annotatedMethod().getAnnotation(AdminAuditLog.class));

        Assertions.assertEquals("ok", result);
        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        Mockito.verify(auditLogService).save(captor.capture());
        Assertions.assertEquals("SUCCESS", captor.getValue().getLogType());
    }

    @Test
    void shouldWriteFailureLogAndRethrowOriginalException() throws Throwable {
        AdminAuditLogService auditLogService = Mockito.mock(AdminAuditLogService.class);
        AdminAuditLogAspect aspect = new AdminAuditLogAspect(auditLogService);
        ProceedingJoinPoint joinPoint = mockJoinPoint(new IllegalStateException("boom"));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("POST");
        request.setRequestURI("/sys-user/reset-password");
        request.addHeader("User-Agent", "JUnit");
        request.setRemoteAddr("127.0.0.1");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> aspect.around(joinPoint, annotatedMethod().getAnnotation(AdminAuditLog.class))
        );

        Assertions.assertEquals("boom", exception.getMessage());
        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        Mockito.verify(auditLogService).save(captor.capture());
        Assertions.assertEquals("FAIL", captor.getValue().getLogType());
    }

    private ProceedingJoinPoint mockJoinPoint(Object outcome) throws Throwable {
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getMethod()).thenReturn(annotatedMethod());
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{new PasswordPayload("old", "new"), List.of(1, 2)});
        if (outcome instanceof Throwable throwable) {
            Mockito.when(joinPoint.proceed()).thenThrow(throwable);
        } else {
            Mockito.when(joinPoint.proceed()).thenReturn(outcome);
        }
        return joinPoint;
    }

    private Method annotatedMethod() {
        try {
            return AuditAnnotatedTarget.class.getDeclaredMethod("operate", PasswordPayload.class, List.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    static class AuditAnnotatedTarget {
        @AdminAuditLog(module = "用户管理", action = "重置密码")
        public void operate(PasswordPayload payload, List<Integer> ids) {
        }
    }

    record PasswordPayload(String oldPassword, String newPassword) {
    }
}
