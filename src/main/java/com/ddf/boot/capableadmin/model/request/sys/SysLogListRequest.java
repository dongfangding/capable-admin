package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

/**
 * 系统日志分页查询请求。
 */
@Data
public class SysLogListRequest implements PageRequest {

    /** 页码。 */
    private Integer pageNum;

    /** 每页条数。 */
    private Integer pageSize;

    /** 操作用户名。 */
    private String username;

    /** 日志类型。 */
    private String logType;

    /** 方法名关键字。 */
    private String method;

    /** 开始时间戳。 */
    private Long startTime;

    /** 结束时间戳。 */
    private Long endTime;
}
