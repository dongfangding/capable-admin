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
 * 系统日志
 */
@Data
public class SysLog {
    /**
     * ID
     */
    private Long logId;

    private String description;

    private String logType;

    private String method;

    private String params;

    private String requestIp;

    private Long time;

    private String username;

    private String phone;

    private String address;

    private String browser;

    private String exceptionDetail;

    private Long createTime;
}
