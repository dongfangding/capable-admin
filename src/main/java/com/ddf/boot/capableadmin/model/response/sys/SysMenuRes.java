package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.util.DateUtils;
import lombok.Data;

/**
* <p>系统菜单</p >
*
* @author Snowball
* @version 1.0
* @since 2025/01/06 13:35
*/
@Data
public class SysMenuRes {
	/**
	 * ID
	 */
	private Long menuId;

	/**
	 * 上级菜单ID，默认0为一级节点
	 */
	private Long pid;

	/**
	 * 菜单类型，catalog：目录；menu：菜单；embedded： 内嵌；link：外链 ；button： 按钮；
	 */
	private String type;

	/**
	 * 菜单名称，一般是前端使用给程序用的，  如Vue Router 路由标识 ，前端用 name 做路由跳转 ，router.push({ name: 'SystemUser' })
	 */
	private String name;

	/**
	 * 菜单标题，给人看的，即界面上显示的菜单标题
	 */
	private String title;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * ，即显示在浏览器地址栏上的路径，实际这个路径对应的显示内容则要看component组件
	 */
	private String path;

	/**
	 * 前端组件，为实际前端路由组件。对应view注册的组件地址，如前端静态路由写法component: () => import('#/views/system/user/list.vue'),常用后端返回"component": "/system/user/list"
	 */
	private String component;

	/**
	 * 排序
	 */
	private Integer sort;

	/**
	 * 是否启用
	 */
	private Boolean enable;

	/**
	 * 菜单元数据，介于菜单可以配置的东西太多，且每个前端都不一样，用这个大json存储，前端要用啥自己存，后端只负责存储。如菜单的图标，是否隐藏，是否缓存，是否外链，是否固定标签页等等等等，各种自定义的用于控制界面表现的参数全放到这个大json里
	 */
	private String meta;

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

    public String getFormatCreateTime() {
        return DateUtils.standardFormatSeconds(createTime);
    }

    public String getFormatUpdateTime() {
        return DateUtils.standardFormatSeconds(updateTime);
    }

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return title;
    }
}
