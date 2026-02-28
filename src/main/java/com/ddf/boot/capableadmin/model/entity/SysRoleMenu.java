package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @since 2026/02/27 17:52
*/


/**
 * 角色菜单关联
 */
@Data
public class SysRoleMenu {
    /**
     * 菜单ID
     */
    private Long menuId;

    /**
     * 角色ID
     */
    private Long roleId;
}
