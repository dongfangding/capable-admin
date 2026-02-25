package com.ddf.boot.capableadmin.model.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/06 20:40
 */
@Data
public class UserRoleMenuDTO implements Serializable {
    /**
     * ID
     */
    private Long roleId;

    /**
     * 名称
     */
    private String name;

    /**
     * 角色级别
     */
    private Integer level;

    /**
     * 描述
     */
    private String description;

    /**
     * ip白名单（指定IP登入）
     */
    private String ipLimit;

    /**
     * 菜单列表
     */
    private List<SimpleMenu> menuList;

}
