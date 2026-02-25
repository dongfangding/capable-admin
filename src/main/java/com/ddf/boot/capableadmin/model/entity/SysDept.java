package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/06 17:56
*/


/**
 * 部门
 */
@Data
public class SysDept {
    /**
     * ID
     */
    private Long deptId;

    /**
     * 上级部门，默认为0，代表一级节点
     */
    private Long pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态, 0 禁用 1 启用
     */
    private Boolean enabled;

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
     * 子节点数量，用来判定是否需要展开
     */
    private Integer subCount;
}
