package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.constraint.collect.ITreeTagCollection;
import jakarta.annotation.Nonnull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
* <p>系统菜单</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/06 17:53
*/
@Data
public class SysMenuNode implements Serializable, ITreeTagCollection<Long, SysMenuNode> {
    /**
     * ID
     */
    private Long menuId;

    /**
     * 上级菜单ID，默认0为一级节点
     */
    private Long pid;

    /**
     * 菜单类型
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

    /**
     * 子节点
     */
    private List<SysMenuNode> children = new ArrayList<>();

    @Override
    public Long getTreeId() {
        return menuId;
    }

    @Override
    public Long getTreeParentId() {
        return pid;
    }

    @Nonnull
    @Override
    public List<SysMenuNode> getChildren() {
        return children;
    }
}
