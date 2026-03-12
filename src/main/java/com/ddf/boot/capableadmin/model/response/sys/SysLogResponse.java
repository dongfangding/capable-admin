package com.ddf.boot.capableadmin.model.response.sys;

import lombok.Data;

/**
 * 系统日志分页查询响应。
 */
@Data
public class SysLogResponse {

    /** 日志ID。 */
    private Long logId;
    /** 操作描述。 */
    private String description;
    /** 日志类型。 */
    private String logType;
    /** 方法签名。 */
    private String method;
    /** 请求参数。 */
    private String params;
    /** 请求IP。 */
    private String requestIp;
    /** 执行耗时。 */
    private Long time;
    /** 操作用户名。 */
    private String username;
    /** 地址。 */
    private String address;
    /** 浏览器信息。 */
    private String browser;
    /** 异常详情。 */
    private String exceptionDetail;
    /** 创建时间。 */
    private Long createTime;
}
