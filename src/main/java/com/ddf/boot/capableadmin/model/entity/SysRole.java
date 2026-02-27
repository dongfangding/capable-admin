package com.ddf.boot.capableadmin.model.entity;

import lombok.Data;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2026/02/27 17:52
*/


/**
 * 角色表
 */
@Data
public class SysRole {
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

    /**
     * 是否超管， 这个超管是虚拟的，如果是的话， 直接拥有全部权限，不需要手动关联子权限，只能初始化，不能接口新增
     */
    private Boolean isAdmin;
}