package com.ddf.boot.capableadmin.service;

import com.ddf.boot.capableadmin.infra.mapper.SysLogMapper;
import com.ddf.boot.capableadmin.model.entity.SysLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class AdminAuditLogServiceTest {

    @Test
    void shouldMaskPasswordFieldsAndTruncateOversizedParams() {
        SysLogMapper sysLogMapper = Mockito.mock(SysLogMapper.class);
        AdminAuditLogService service = new AdminAuditLogService(sysLogMapper);
        SysLog log = new SysLog();
        log.setParams("{\"password\":\"abc\",\"payload\":\"" + "x".repeat(5000) + "\"}");

        service.save(log);

        ArgumentCaptor<SysLog> captor = ArgumentCaptor.forClass(SysLog.class);
        Mockito.verify(sysLogMapper).insertSelective(captor.capture());
        String params = captor.getValue().getParams();
        Assertions.assertFalse(params.contains("abc"));
        Assertions.assertTrue(params.contains("***"));
        Assertions.assertTrue(params.length() <= 2048);
    }
}
