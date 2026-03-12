package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 字典明细新增或修改请求。
 */
@Data
public class SysDictDetailPersistRequest {

	/**
	 * 明细ID，新增时为空。
	 */
	private Long detailId;
	/**
	 * 字典ID。
	 */
	@NotNull
	private Long dictId;
	/**
	 * 明细字典编码
	 */
	@NotBlank(message = "字典编码不能为空")
	private String detailCode;
	/**
	 * 显示标签。
	 */
	@NotBlank
	private String label;
	/**
	 * 实际值。
	 */
	@NotBlank
	private String value;
	/**
	 * 排序值。
	 */
	@NotNull
	private Integer dictSort;
}
