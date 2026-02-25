package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;

/**
 * <p>更新系统角色菜单</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/07 18:07
 */
@Data
public class SysRoleMenuUpdateRequest {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    /**
     * 菜单id集合
     */
    private Set<Long> menuIds;
}
