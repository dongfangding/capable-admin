package com.ddf.boot.capableadmin.model.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/06 20:46
 */
@Data
public class SimpleMenu implements Serializable {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 菜单权限
     */
    private String permission;

}
