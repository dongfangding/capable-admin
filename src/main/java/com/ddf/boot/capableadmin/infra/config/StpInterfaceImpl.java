package com.ddf.boot.capableadmin.infra.config;

import cn.dev33.satoken.stp.StpInterface;
import com.ddf.boot.capableadmin.infra.util.PrettyAdminSecurityUtils;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Sa-Token 权限与角色加载实现。
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 获取当前登录用户权限码集合。
     *
     * @param loginId 登录ID
     * @param loginType 登录类型
     * @return 权限码列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return new ArrayList<>(PrettyAdminSecurityUtils.getCurrentUser().getPermissions());
    }

    /**
     * 获取当前登录用户角色集合。
     *
     * @param loginId 登录ID
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return new ArrayList<>(PrettyAdminSecurityUtils.getCurrentUser().getRoles());
    }
}
