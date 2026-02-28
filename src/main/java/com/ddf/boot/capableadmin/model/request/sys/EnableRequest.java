package com.ddf.boot.capableadmin.model.request.sys;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/28 17:44
 */
@Data
public class EnableRequest {

	/**
	 * 要修改的记录id
	 */
	@NotNull(message = "enable not null")
	private Long id;

	/**
	 * 是否开启
	 */
	@NotNull(message = "enable not null")
	private Boolean enable;
}
