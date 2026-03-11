package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysLogApplicationService;
import com.ddf.boot.capableadmin.model.request.sys.SysLogListRequest;
import com.ddf.boot.capableadmin.model.response.sys.SysLogResponse;
import com.ddf.boot.common.api.model.common.request.BatchIdRequest;
import com.ddf.boot.common.api.model.common.response.PageResult;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SysLogControllerTest {

    @Test
    void listShouldDelegateToApplicationService() {
        SysLogApplicationService applicationService = Mockito.mock(SysLogApplicationService.class);
        SysLogController controller = new SysLogController(applicationService);
        SysLogListRequest request = new SysLogListRequest();
        request.setUsername("tester");
        PageResult<SysLogResponse> pageResult = new PageResult<>();
        pageResult.setContent(List.of(new SysLogResponse()));
        Mockito.when(applicationService.list(request)).thenReturn(pageResult);

        PageResult<SysLogResponse> response = controller.list(request).getData();

        Assertions.assertEquals(1, response.getContent().size());
    }

    @Test
    void deleteShouldDelegateToApplicationService() {
        SysLogApplicationService applicationService = Mockito.mock(SysLogApplicationService.class);
        SysLogController controller = new SysLogController(applicationService);
        BatchIdRequest request = new BatchIdRequest();
        request.setIds(Set.of(1L, 2L));

        Boolean result = controller.delete(request).getData();

        Assertions.assertTrue(result);
        Mockito.verify(applicationService).delete(Set.of(1L, 2L));
    }
}
