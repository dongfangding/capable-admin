package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysRoleMenu;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
* <p>description</p >
*
* @author Snowball
* @version 1.0
* @date 2025/01/07 18:13
*/
public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(@Param("menuId") Long menuId, @Param("roleId") Long roleId);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    /**
     * 根据菜单ID集合删除角色菜单
     *
     * @param menuIds
     * @return
     */
    int deleteByMenuIdList(@Param("menuIds") Set<Long> menuIds);

    /**
     * 根据角色删除菜单
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据角色删除菜单
     *
     * @param roleIds
     * @return
     */
    int deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 插入角色菜单
     *
     * @param roleId
     * @param menuIdList
     * @return
     */
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") Set<Long> menuIdList);
}
