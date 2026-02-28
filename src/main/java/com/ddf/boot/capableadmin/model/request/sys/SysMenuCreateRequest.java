package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.capableadmin.enums.MenuTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
* <p>系统菜单</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/04 20:27
*/
@Data
public class SysMenuCreateRequest {

    /**
     * ID
     */
    private Long menuId;

    /**
     * 上级菜单ID，默认0为一级节点
     */
    private Long pid = 0L;

    /**
     * 菜单类型
     */
    @NotNull(message = "菜单类型不能为空")
    private MenuTypeEnum type;

    /**
     * 菜单标题
     */
    @NotBlank(message = "菜单标题不能为空")
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
    @NotNull(message = "菜单排序不能为空")
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
}
