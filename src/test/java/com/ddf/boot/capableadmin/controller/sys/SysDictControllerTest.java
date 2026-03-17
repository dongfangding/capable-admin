package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysDictApplicationService;
import com.ddf.boot.capableadmin.model.request.sys.SysDictListRequest;
import com.ddf.boot.capableadmin.model.request.sys.SysDictPersistRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysDictResponse;
import com.ddf.boot.common.api.model.common.request.IdRequest;
import com.ddf.boot.common.api.model.common.response.PageResult;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SysDictControllerTest {

    @Test
    void listShouldDelegateToApplicationService() {
        SysDictApplicationService applicationService = Mockito.mock(SysDictApplicationService.class);
        SysDictController controller = new SysDictController(applicationService);
        SysDictListRequest request = new SysDictListRequest();
        request.setKeyword("status");
        PageResult<SysDictResponse> pageResult = new PageResult<>();
        pageResult.setContent(List.of(new SysDictResponse()));
        Mockito.when(applicationService.listDict(request)).thenReturn(pageResult);

        PageResult<SysDictResponse> response = controller.list(request).getData();

        Assertions.assertEquals(1, response.getContent().size());
    }

    @Test
    void persistAndDeleteShouldDelegateToApplicationService() {
        SysDictApplicationService applicationService = Mockito.mock(SysDictApplicationService.class);
        SysDictController controller = new SysDictController(applicationService);
        SysDictPersistRequest persistRequest = new SysDictPersistRequest();
        persistRequest.setName("status");
        persistRequest.setDescription("dict");
        IdRequest idRequest = new IdRequest();
        idRequest.setId(1L);

        Assertions.assertTrue(controller.persist(persistRequest).getData());
        Assertions.assertTrue(controller.delete(idRequest).getData());

        Mockito.verify(applicationService).persistDict(persistRequest);
        Mockito.verify(applicationService).deleteDict(1L);
    }
}
