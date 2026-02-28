package com.ddf.boot.capableadmin.model.cqrs;

import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/27 18:23
 */
@Data
public class SysRoleAdminResult {

	private Long roleId;

	private String name;

	private Boolean isAdmin;

	private Integer level;
}
