package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/28 13:19
 */
@Data
public class SysRoleListRequest implements PageRequest {

	private Integer pageNum;
	private Integer pageSize;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 是否启用， 0否1是
	 */
	private Boolean enable;

	/**
	 * 开始时间
	 */
	private Long startTime;

	/**
	 * 结束时间
	 */
	private Long endTime;

	/**
	 * 描述
	 */
	private String description;
}
