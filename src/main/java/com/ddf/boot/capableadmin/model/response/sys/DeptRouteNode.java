package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.capableadmin.enums.MenuTypeEnum;
import com.ddf.boot.common.api.constraint.collect.ITreeTagCollection;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 构建菜单路由节点（左侧菜单栏）
 *
 * @author snowball
 * @since 2025/1/6 下午8:28
 **/
@Data
public class DeptRouteNode implements Serializable, ITreeTagCollection<Long, DeptRouteNode> {
    /**
     * ID
     */
    private Long menuId;

	private Integer status = 1;

    /**
     * 上级菜单ID，默认0为一级节点
     */
    private Long pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 跳转路径
     */
    private String path;

	/**
	 * 一般都是path, 当详情页不在左侧菜单中显示，但需要高亮父菜单时使用。左侧菜单，用户管理 (path: /system/user, activePath: /system/user/***)，访问 /system/user/100 时：
	 * - path 是 /system/user/100，匹配不到菜单
	 * - 但 activePath 是 /system/user/***，能匹配上
	 * - 所以"用户管理"菜单保持高亮
	 */
	private String activePath;

    /**
     * 组件
     */
    private String component;

    /**
     * 菜单元数据
     */
    private Map<String, Object> meta;

	/**
	 * 菜单类型
	 * @see MenuTypeEnum
	 */
	private String type;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 权限
	 */
	private String permission;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	public Long getKey() {
		return menuId;
	}

    /**
     * 子节点
     */
    private List<DeptRouteNode> children;

    @Override
    public Long getTreeId() {
        return menuId;
    }

    @Override
    public Long getTreeParentId() {
        return pid;
    }
}
