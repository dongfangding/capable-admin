package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>角色菜单关联</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/07 18:13
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
