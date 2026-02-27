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
 * 系统菜单
 */
@Data
public class SysMenu {
    /**
     * ID
     */
    private Long menuId;

    /**
     * 上级菜单ID，默认0为一级节点
     */
    private Long pid;

    /**
     * 菜单类型0：目录 1：菜单 2：按钮
     */
    private Integer type;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 组件名称
     */
    private String componentName;

    /**
     * 组件
     */
    private String component;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接地址
     */
    private String path;

    /**
     * 是否外链
     */
    private Boolean isFrame;

    /**
     * 缓存
     */
    private Boolean cache;

    /**
     * 隐藏
     */
    private Boolean hidden;

    /**
     * 权限
     */
    private String permission;

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
     * 子节点数量
     */
    private Integer subCount;
}