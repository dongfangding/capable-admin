package com.ddf.boot.capableadmin.model.response.sys;

import java.util.Date;
import lombok.Data;

/**
 * 字典明细分页响应。
 */
@Data
public class SysDictDetailResponse {
	/**
	 * 明细ID。
	 */
	private Long detailId;
	/**
	 * 字典ID。
	 */
	private Long dictId;
	/**
	 * 显示标签。
	 */
	private String label;
	/**
	 * 实际值。
	 */
	private String value;
	/**
	 * 明细编码
	 */
	private String detailCode;
	/**
	 * 排序值。
	 */
	private Integer dictSort;
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
