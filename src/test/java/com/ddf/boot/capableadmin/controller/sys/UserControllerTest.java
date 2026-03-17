package com.ddf.boot.capableadmin.controller.sys;

import com.ddf.boot.capableadmin.application.SysUserApplicationService;
import com.ddf.boot.capableadmin.infra.audit.AdminAuditLog;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import com.ddf.boot.capableadmin.model.dto.PrettyAdminUserDetails;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.request.sys.UserChangePasswordRequest;
import com.ddf.boot.capableadmin.model.request.sys.UserProfileUpdateRequest;
import com.ddf.boot.capableadmin.model.response.sys.UserProfileResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class UserControllerTest {

    @Test
    void writeEndpointsShouldBeAnnotatedForAudit() throws NoSuchMethodException {
        Assertions.assertNotNull(
                UserController.class.getDeclaredMethod("updateProfile", UserProfileUpdateRequest.class)
                        .getAnnotation(AdminAuditLog.class)
        );
        Assertions.assertNotNull(
                UserController.class.getDeclaredMethod("changePassword", UserChangePasswordRequest.class)
                        .getAnnotation(AdminAuditLog.class)
        );
    }

    @Test
    void profileShouldReturnCurrentUserProfile() {
        SysUserApplicationService applicationService = Mockito.mock(SysUserApplicationService.class);
        UserController controller = new UserController(applicationService);
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            PrettyAdminUserDetails userDetails = buildDetails();
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUser).thenReturn(userDetails);
            Mockito.when(applicationService.getCurrentUserProfile(1L)).thenReturn(buildProfileResponse());

            UserProfileResponse response = controller.profile().getData();

            Assertions.assertEquals("tester", response.getUsername());
            Assertions.assertEquals("tester@example.com", response.getEmail());
        }
    }

    @Test
    void updateProfileShouldDelegateToApplicationService() {
        SysUserApplicationService applicationService = Mockito.mock(SysUserApplicationService.class);
        UserController controller = new UserController(applicationService);
        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setNickname("tester");
        request.setEmail("tester@example.com");
        request.setMobile("13800000000");
        request.setAvatar("https://cdn.example/avatar.png");
        request.setSex(1);
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUserId).thenReturn(1L);

            Boolean result = controller.updateProfile(request).getData();

            Assertions.assertTrue(result);
            Mockito.verify(applicationService).updateCurrentUserProfile(1L, request);
        }
    }

    @Test
    void changePasswordShouldDelegateToApplicationService() {
        SysUserApplicationService applicationService = Mockito.mock(SysUserApplicationService.class);
        UserController controller = new UserController(applicationService);
        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("new");
        try (MockedStatic<PrettyAdminSecurityUtils> mockedStatic = Mockito.mockStatic(PrettyAdminSecurityUtils.class)) {
            mockedStatic.when(PrettyAdminSecurityUtils::getCurrentUserId).thenReturn(1L);

            Boolean result = controller.changePassword(request).getData();

            Assertions.assertTrue(result);
            Mockito.verify(applicationService).changeCurrentUserPassword(1L, request);
        }
    }

    private PrettyAdminUserDetails buildDetails() {
        SysUser user = new SysUser();
        user.setUserId(1L);
        user.setUsername("tester");
        user.setNickname("tester");
        user.setPassword("pwd");
        user.setEnabled(Boolean.TRUE);
        return new PrettyAdminUserDetails(user, java.util.Set.of("admin"), java.util.Set.of("System:User:Edit"), 1, false);
    }

    private UserProfileResponse buildProfileResponse() {
        UserProfileResponse response = new UserProfileResponse();
        response.setUserId(1L);
        response.setUsername("tester");
        response.setNickname("tester");
        response.setEmail("tester@example.com");
        response.setMobile("13800000000");
        response.setAvatar("https://cdn.example/avatar.png");
        response.setSex(1);
        return response;
    }
}
