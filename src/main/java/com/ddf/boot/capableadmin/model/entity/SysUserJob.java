package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/08 20:18
*/
@Data
public class SysUserJob {
    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 岗位ID
    */
    private Long jobId;
}
