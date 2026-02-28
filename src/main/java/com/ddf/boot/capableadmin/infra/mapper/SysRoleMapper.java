package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.cqrs.SysRoleAdminResult;
import com.ddf.boot.capableadmin.model.dto.UserRoleMenuDTO;
import com.ddf.boot.capableadmin.model.entity.SysRole;
import com.ddf.boot.capableadmin.model.request.sys.SysRoleListRequest;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/28 14:27
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
    List<SysRole> listAll(SysRoleListRequest request);

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
    Set<SysRoleAdminResult> findRoleNamesByUserId(@Param("userId") Long userId);

	/**
	 * 更新启用状态
	 *
	 * @param roleId
	 * @param enable
	 * @return
	 */
	int updateEnable(@Param("roleId") Long roleId, @Param("enable") Boolean enable);
}
