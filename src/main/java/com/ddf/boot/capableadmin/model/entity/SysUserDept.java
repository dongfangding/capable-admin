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
 * 用户部门关联
 */
@Data
public class SysUserDept {
    /**
     * 用户id
     */
    private Long userId;

    private Long deptId;
}
