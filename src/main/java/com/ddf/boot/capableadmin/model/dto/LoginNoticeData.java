package com.ddf.boot.capableadmin.model.dto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/09/10 12:01
 */
@Data
public class LoginNoticeData implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Long userId;

	private String username;

	private String nickname;

}
