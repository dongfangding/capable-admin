package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/27 18:31
 */
public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    SysUser selectByUsername(@Param("username") String username);

    SysUser selectByEmail(@Param("email") String email);

    SysUser selectByMobile(@Param("mobile") String mobile);

    /**
     * 根据角色ID列表查询用户ID列表
     *
     * @param roleIds 角色ID列表
     * @return 用户ID列表
     */
    List<Long> findUserIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色ID查询用户ID列表
     *
     * @param roleId 角色ID
     * @return 用户ID列表
     */
    List<Long> findUserIdsByRoleId(@Param("roleId") Long roleId);
}