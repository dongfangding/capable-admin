package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 字典新增或修改请求。
 */
@Data
public class SysDictPersistRequest {

	/**
	 * 字典ID，新增时为空。
	 */
	private Long dictId;
	/**
	 * 字典名称。
	 */
	@NotBlank(message = "name not null")
	private String name;
	/**
	 * 字典编码
	 */
	@NotBlank(message = "dictCode not null")
	private String dictCode;
	/**
	 * 字典描述。
	 */
	private String description;
}
