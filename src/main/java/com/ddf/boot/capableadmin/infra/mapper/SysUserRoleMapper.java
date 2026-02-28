package com.ddf.boot.capableadmin.infra.mapper;

import com.ddf.boot.capableadmin.model.entity.SysUserRole;
import java.util.Set;
import org.apache.ibatis.annotations.Param;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 17:52
 */
public interface SysUserRoleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    int countByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * 删除角色与用户的关联关系
     *
     * @param roleIds
     * @return
     */
    int deleteByRoleIds(@Param("roleIds") Set<Long> roleIds);

    int batchInsertUserRole(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);

    void deleteByUserId(@Param("userId") Long userId);
}
