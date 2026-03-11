package com.ddf.boot.capableadmin.controller.auth;

import com.ddf.boot.capableadmin.application.AuthApplicationService;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

class AuthControllerTest {

    @Test
    void codesShouldReturnCurrentUserPermissions() {
        AuthController controller = new AuthController(Mockito.mock(AuthApplicationService.class));
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser)
                    .thenReturn(buildDetails(100L, Set.of("System:Menu:List", "System:User:Edit"), Set.of("admin")));

            List<String> codes = controller.codes().getData();

            Assertions.assertTrue(codes.contains("System:Menu:List"));
            Assertions.assertTrue(codes.contains("System:User:Edit"));
        }
    }

    private PrettyAdminUserDetails buildDetails(Long userId, Set<String> permissions, Set<String> roles) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setUsername("tester");
        sysUser.setNickname("tester");
        sysUser.setPassword("pwd");
        sysUser.setEnabled(Boolean.TRUE);
        return new PrettyAdminUserDetails(sysUser, roles, permissions, 1);
    }
}
