package com.ddf.boot.capableadmin.mapper;

import com.ddf.boot.capableadmin.model.dto.UserRoleMenuDTO;
import com.ddf.boot.capableadmin.model.entity.SysRole;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * description
 * </p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/07 17:55
 */
public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    SysRole findByRoleName(@Param("name") String name);

    /**
     * 返回所有角色
     *
     * @return
     */
    List<SysRole> listAll();

    /**
     * 查询用户的所有角色和菜单信息
     *
     * @param userId
     * @return
     */
    List<UserRoleMenuDTO> findByUserId(@Param("userId") Long userId);

    /**
     * 查询用户所有的角色id
     *
     * @param userId
     * @return
     */
    List<Long> getUserAllRoleIds(@Param("userId") Long userId);

    /**
     * 根据角色id删除
     *
     * @param roleIds
     * @return
     */
    int deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 根据菜单ID查询关联的角色ID列表
     *
     * @param menuId 菜单ID
     * @return 角色ID列表
     */
    List<Long> findRoleIdsByMenuId(@Param("menuId") Long menuId);

    /**
     * 查询用户的所有角色名称
     *
     * @param userId 用户ID
     * @return 角色名称集合
     */
    Set<String> findRoleNamesByUserId(@Param("userId") Long userId);
}
