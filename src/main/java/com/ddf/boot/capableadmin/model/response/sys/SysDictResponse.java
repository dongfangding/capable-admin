package com.ddf.boot.capableadmin.model.response.sys;

import java.util.Date;
import lombok.Data;

/**
 * 字典分页响应。
 *
 * @author snowball
 */
@Data
public class SysDictResponse {
	/**
	 * 字典ID。
	 */
	private Long dictId;
	/**
	 * 字典名称。
	 */
	private String name;
	/**
	 * 字典编码
	 */
	private String dictCode;
	/**
	 * 字典描述。
	 */
	private String description;
	/**
	 * 创建人。
	 */
	private String createBy;
	/**
	 * 更新人。
	 */
	private String updateBy;
	/**
	 * 创建时间。
	 */
	private Date createTime;
	/**
	 * 更新时间。
	 */
	private Date updateTime;
}
