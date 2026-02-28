package com.ddf.boot.capableadmin.model.request.sys;

import lombok.Data;

/**
 * <p>菜单同级上级查询</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 16:00
 */
@Data
public class SysMenuSuperiorQuery {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 是否排除自己及子节点， 编辑时使用，不让自己选到自己及子节点
     */
    private boolean excludeSelfAndSub;

}
