package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.infra.mapper.ext.SysLogExtMapper;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SysLogApplicationServiceTest {

    @Test
    void deleteShouldIgnoreEmptyIds() {
        SysLogExtMapper extMapper = Mockito.mock(SysLogExtMapper.class);
        SysLogApplicationService service = new SysLogApplicationService(extMapper);

        service.delete(Set.of());

        Mockito.verify(extMapper, Mockito.never()).deleteByIds(Mockito.anySet());
    }
}
