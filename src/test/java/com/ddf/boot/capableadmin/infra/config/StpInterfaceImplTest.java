package com.ddf.boot.capableadmin.infra.config;

import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class StpInterfaceImplTest {

    private final StpInterfaceImpl stpInterface = new StpInterfaceImpl();

    @Test
    void shouldReadPermissionsAndRolesFromCurrentUserDetails() {
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser)
                    .thenReturn(buildDetails(101L, Set.of("System:Role:List"), Set.of("admin", "super-admin")));

            List<String> permissions = stpInterface.getPermissionList(101L, "login");
            List<String> roles = stpInterface.getRoleList(101L, "login");

            Assertions.assertEquals(List.of("System:Role:List"), permissions);
            Assertions.assertTrue(roles.contains("admin"));
            Assertions.assertTrue(roles.contains("super-admin"));
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
