package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @since 2026/03/12 18:41
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

    /**
     * 描述
     */
    private String description;

    /**
     * 结果
     */
    private String logType;

    /**
     * 方法
     */
    private String method;

    /**
     * 入参
     */
    private String params;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 耗时
     */
    private Long time;

    /**
     * 用户名
     */
    private String username;

    /**
     * 地址信息
     */
    private String address;

    /**
     * user_agent
     */
    private String browser;

    /**
     * 异常详情
     */
    private String exceptionDetail;

    /**
     * 创建时间
     */
    private Long createTime;
}