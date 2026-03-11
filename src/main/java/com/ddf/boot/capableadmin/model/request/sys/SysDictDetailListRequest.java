package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

/**
 * 字典明细分页查询请求。
 */
@Data
public class SysDictDetailListRequest implements PageRequest {

    /** 页码。 */
    private Integer pageNum;
    /** 每页条数。 */
    private Integer pageSize;
    /** 所属字典ID。 */
    private Long dictId;
    /** 标签。 */
    private String label;
    /** 值。 */
    private String value;
}
