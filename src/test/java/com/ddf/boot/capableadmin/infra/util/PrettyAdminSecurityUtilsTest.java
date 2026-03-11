package com.ddf.boot.capableadmin.infra.util;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.ddf.boot.common.api.exception.UnauthorizedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class PrettyAdminSecurityUtilsTest {

    @Test
    void getCurrentUserShouldThrowUnauthorizedWhenSessionUserDetailsMissing() {
        SaSession session = Mockito.mock(SaSession.class);
        Mockito.when(session.get(Mockito.anyString())).thenReturn(null);
        try (MockedStatic<StpUtil> mockedStatic = Mockito.mockStatic(StpUtil.class)) {
            mockedStatic.when(StpUtil::checkLogin).thenAnswer(invocation -> null);
            mockedStatic.when(StpUtil::getLoginIdAsLong).thenReturn(102L);
            mockedStatic.when(() -> StpUtil.getSession(true)).thenReturn(session);

            Assertions.assertThrows(UnauthorizedException.class, PrettyAdminSecurityUtils::getCurrentUser);
        }
    }
}
