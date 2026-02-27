package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/27 17:52
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