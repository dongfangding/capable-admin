package com.ddf.boot.capableadmin.model.entity;

import java.util.Date;
import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2026/02/27 17:52
*/


/**
 * 数据字典详情
 */
@Data
public class SysDictDetail {
    /**
     * ID
     */
    private Long detailId;

    /**
     * 字典id
     */
    private Long dictId;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private Integer dictSort;

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