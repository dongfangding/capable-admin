package com.ddf.boot.capableadmin.application;

import com.ddf.boot.capableadmin.infra.mapper.SysDeptMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserDeptMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserJobMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserMapper;
import com.ddf.boot.capableadmin.infra.mapper.SysUserRoleMapper;
import com.ddf.boot.capableadmin.model.entity.SysUser;
import com.ddf.boot.capableadmin.model.request.sys.UserChangePasswordRequest;
import com.ddf.boot.capableadmin.model.request.sys.UserProfileUpdateRequest;
import com.ddf.boot.capableadmin.service.PrettyAdminCacheManager;
import com.ddf.boot.capableadmin.service.SysDeptService;
import com.ddf.boot.common.api.exception.BusinessException;
import com.ddf.boot.common.core.encode.BCryptPasswordEncoder;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SysUserApplicationServiceTest {

    @Test
    void updateCurrentUserProfileShouldClearCache() {
        SysUserMapper sysUserMapper = Mockito.mock(SysUserMapper.class);
        PrettyAdminCacheManager cacheManager = Mockito.mock(PrettyAdminCacheManager.class);
        SysUserApplicationService service = new SysUserApplicationService(
                Mockito.mock(SysDeptMapper.class),
                Mockito.mock(SysDeptService.class),
                Mockito.mock(SysUserDeptMapper.class),
                sysUserMapper,
                Mockito.mock(SysUserJobMapper.class),
                Mockito.mock(SysUserRoleMapper.class),
                new BCryptPasswordEncoder(),
                cacheManager
        );
        SysUser sysUser = new SysUser();
        sysUser.setUserId(1L);
        sysUser.setUsername("tester");
        sysUser.setNickname("old");
        sysUser.setEnabled(Boolean.TRUE);
        Mockito.when(sysUserMapper.selectByPrimaryKey(1L)).thenReturn(sysUser);
        Mockito.when(sysUserMapper.selectByEmail("new@example.com")).thenReturn(null);
        Mockito.when(sysUserMapper.selectByMobile("13800000000")).thenReturn(null);

        UserProfileUpdateRequest request = new UserProfileUpdateRequest();
        request.setNickname("new");
        request.setEmail("new@example.com");
        request.setMobile("13800000000");
        request.setAvatar("https://cdn.example/avatar.png");
        request.setSex(1);

        service.updateCurrentUserProfile(1L, request);

        Mockito.verify(cacheManager).cleanUserAllCache(1L);
        Mockito.verify(sysUserMapper).updateByPrimaryKeySelective(sysUser);
    }

    @Test
    void changeCurrentUserPasswordShouldRejectWhenOldPasswordDoesNotMatch() {
        SysUserMapper sysUserMapper = Mockito.mock(SysUserMapper.class);
        PrettyAdminCacheManager cacheManager = Mockito.mock(PrettyAdminCacheManager.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        SysUserApplicationService service = new SysUserApplicationService(
                Mockito.mock(SysDeptMapper.class),
                Mockito.mock(SysDeptService.class),
                Mockito.mock(SysUserDeptMapper.class),
                sysUserMapper,
                Mockito.mock(SysUserJobMapper.class),
                Mockito.mock(SysUserRoleMapper.class),
                passwordEncoder,
                cacheManager
        );
        SysUser sysUser = new SysUser();
        sysUser.setUserId(2L);
        sysUser.setUsername("tester");
        sysUser.setPassword(passwordEncoder.encode("right-password"));
        sysUser.setEnabled(Boolean.TRUE);
        Mockito.when(sysUserMapper.selectByPrimaryKey(2L)).thenReturn(sysUser);

        UserChangePasswordRequest request = new UserChangePasswordRequest();
        request.setOldPassword("wrong-password");
        request.setNewPassword("new-password");

        Assertions.assertThrows(BusinessException.class, () -> service.changeCurrentUserPassword(2L, request));
        Mockito.verify(cacheManager, Mockito.never()).cleanUserAllCache(2L);
    }
}
