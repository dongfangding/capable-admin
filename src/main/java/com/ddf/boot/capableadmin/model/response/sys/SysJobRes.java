package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.util.DateUtils;
import lombok.Data;

/**
* <p>岗位</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/03 17:34
*/
@Data
public class SysJobRes {
    /**
     * ID
     */
    private Long jobId;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 岗位状态
     */
    private Boolean enabled;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建日期
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    public String getFormatCreateTime() {
        return DateUtils.standardFormatSeconds(createTime);
    }

    public String getFormatUpdateTime() {
        return DateUtils.standardFormatSeconds(updateTime);
    }
}
