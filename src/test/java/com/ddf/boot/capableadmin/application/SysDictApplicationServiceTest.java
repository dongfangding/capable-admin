package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.infra.mapper.SysDictDetailMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysDictMapper;
import com.ddf.boot.capableadmin.infra.mapper.ext.SysDictDetailExtMapper;
import com.ddf.boot.capableadmin.infra.mapper.ext.SysDictExtMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class SysDictApplicationServiceTest {

    @Test
    void deleteDictShouldDeleteDetailsFirst() {
        SysDictMapper sysDictMapper = Mockito.mock(SysDictMapper.class);
        SysDictDetailMapper sysDictDetailMapper = Mockito.mock(SysDictDetailMapper.class);
        SysDictExtMapper sysDictExtMapper = Mockito.mock(SysDictExtMapper.class);
        SysDictDetailExtMapper sysDictDetailExtMapper = Mockito.mock(SysDictDetailExtMapper.class);
        SysDictApplicationService service = new SysDictApplicationService(
                sysDictMapper,
                sysDictDetailMapper,
                sysDictExtMapper,
                sysDictDetailExtMapper
        );

        service.deleteDict(1L);

        InOrder inOrder = Mockito.inOrder(sysDictDetailExtMapper, sysDictMapper);
        inOrder.verify(sysDictDetailExtMapper).deleteByDictId(1L);
        inOrder.verify(sysDictMapper).deleteByPrimaryKey(1L);
    }
}
