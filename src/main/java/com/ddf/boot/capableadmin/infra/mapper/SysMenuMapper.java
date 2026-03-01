package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysMenu;
import com.ddf.boot.capableadmin.model.request.sys.SysMenuListQuery;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/03/01 22:38
 */
public interface SysMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> findByIds(@Param("menuIds") Set<Long> menuIds);

    List<SysMenu> findByPid(@Param("pid") Long pid);

    List<SysMenu> findByPidList(@Param("pidList") Set<Long> pidList);

    SysMenu findByTitle(@Param("title") String title);

    SysMenu findByName(@Param("name") String name);

    int countByPid(@Param("pid") Long pid);

    void updateSubCount(@Param("menuId") Long menuId, @Param("count") int count);

    /**
     * 列表查询
     *
     * @param query
     * @return
     */
    List<SysMenu> list(SysMenuListQuery query);

    /**
     * 根据菜单id集合删除
     *
     * @param menuIds
     * @return
     */
    int deleteByMenuIds(@Param("menuIds") Set<Long> menuIds);

    /**
     * 查询所有按钮以外的所有的菜单， 排除按钮是为了这个只是为了构建菜单树时使用的， 给超管用
     *
     * @return
     */
    List<SysMenu> getAdminUserAllMenuExcludeBtn();

    /**
     * 查询用户排除按钮以外的所有的菜单，排除按钮是为了这个只是为了构建菜单树时使用的
     *
     * @param userId
     * @return
     */
    List<SysMenu> getUserAllMenuExcludeBtn(@Param("userId") Long userId);

    /**
     * 查询所有按钮以外的所有的菜单， 排除按钮是为了这个只是为了构建菜单树时使用的， 给超管用
     *
     * @return
     */
    List<SysMenu> getAdminUserAllMenu();

    /**
     * 查询用户排除按钮以外的所有的菜单，排除按钮是为了这个只是为了构建菜单树时使用的
     *
     * @param userId
     * @return
     */
    List<SysMenu> getUserAllMenu(@Param("userId") Long userId);

    int batchInsert(List<SysMenu> record);

    /**
     * 查询用户的所有权限标识
     *
     * @param userId 用户ID
     * @return 权限标识集合
     */
    Set<String> findPermissionsByUserId(@Param("userId") Long userId);

    /**
     * 查询所有权限标识, 给超管用
     *
     * @return 权限标识集合
     */
    Set<String> findAllPermissions();

    /**
     * 查询角色列表的所有权限标识
     *
     * @param roleIds 角色ID列表
     * @return 权限标识集合
     */
    Set<String> findPermissionsByRoleIds(@Param("roleIds") List<Long> roleIds);
}