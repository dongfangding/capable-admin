package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

/**
 * 字典分页查询请求。
 */
@Data
public class SysDictListRequest implements PageRequest {

    /** 页码。 */
    private Integer pageNum;
    /** 每页条数。 */
    private Integer pageSize;
    /** 字典名称。 */
    private String name;
}
