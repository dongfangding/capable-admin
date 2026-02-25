package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.util.DateUtils;
import lombok.Data;

/**
* <p>角色表</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/03 16:50
*/
@Data
public class SysRoleRes {
    /**
     * ID
     */
    private Long roleId;

    /**
     * 名称
     */
    private String name;

    /**
     * 角色级别
     */
    private Integer level;

    /**
     * 描述
     */
    private String description;

    /**
     * ip白名单（指定IP登入）
     */
    private String ipLimit;

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

    /**
     * 排序
     */
    private Integer sort;

    public String getFormatCreateTime() {
        return DateUtils.standardFormatSeconds(createTime);
    }

    public String getFormatUpdateTime() {
        return DateUtils.standardFormatSeconds(updateTime);
    }
}
