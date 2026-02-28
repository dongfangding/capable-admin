package com.ddf.boot.capableadmin.model.request.sys;

import lombok.Data;

/**
 * <p>菜单列表查询</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 16:00
 */
@Data
public class SysMenuListQuery {

    /**
     * 关键字，支持标题、组件、权限
     */
    private String keyword;

    /**
     * 上级部门，默认为0，代表一级节点
     */
    private Long pid;

    /**
     * 是否排除自己及子节点， 编辑部门时使用，不让自己选到自己及子节点
     */
    private boolean excludeSelfAndSub;

}
