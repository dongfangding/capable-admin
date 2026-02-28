package com.ddf.boot.capableadmin.model.entity;

import java.util.Date;
import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @since 2026/02/27 17:52
*/


/**
 * 数据字典
 */
@Data
public class SysDict {
    /**
     * ID
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
