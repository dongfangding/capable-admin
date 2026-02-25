package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/07 20:54
*/
/**
 * 用户部门关联
 */
@Data
public class SysUserDept {
    /**
    * 用户id
    */
    private Long userId;

    /**
    * 部门id
    */
    private Long deptId;
}
