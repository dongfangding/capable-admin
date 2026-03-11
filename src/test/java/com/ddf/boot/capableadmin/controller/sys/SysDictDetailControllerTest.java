package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysDictApplicationService;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictDetailPersistRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictDetailResponse;
import com.ddf.boot.common.api.model.common.request.IdRequest;
import com.ddf.boot.common.api.model.common.response.PageResult;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SysDictDetailControllerTest {

    @Test
    void listShouldDelegateToApplicationService() {
        SysDictApplicationService applicationService = Mockito.mock(SysDictApplicationService.class);
        SysDictDetailController controller = new SysDictDetailController(applicationService);
        SysDictDetailListRequest request = new SysDictDetailListRequest();
        request.setDictId(1L);
        PageResult<SysDictDetailResponse> pageResult = new PageResult<>();
        pageResult.setContent(List.of(new SysDictDetailResponse()));
        Mockito.when(applicationService.listDictDetail(request)).thenReturn(pageResult);

        PageResult<SysDictDetailResponse> response = controller.list(request).getData();

        Assertions.assertEquals(1, response.getContent().size());
    }

    @Test
    void persistAndDeleteShouldDelegateToApplicationService() {
        SysDictApplicationService applicationService = Mockito.mock(SysDictApplicationService.class);
        SysDictDetailController controller = new SysDictDetailController(applicationService);
        SysDictDetailPersistRequest persistRequest = new SysDictDetailPersistRequest();
        persistRequest.setDictId(1L);
        persistRequest.setLabel("正常");
        persistRequest.setValue("1");
        persistRequest.setDictSort(1);
        IdRequest idRequest = new IdRequest();
        idRequest.setId(2L);

        Assertions.assertTrue(controller.persist(persistRequest).getData());
        Assertions.assertTrue(controller.delete(idRequest).getData());

        Mockito.verify(applicationService).persistDictDetail(persistRequest);
        Mockito.verify(applicationService).deleteDictDetail(2L);
    }
}
