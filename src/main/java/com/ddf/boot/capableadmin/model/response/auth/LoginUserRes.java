package com.ddf.boot.capableadmin.model.response.auth;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 20:35
 */
@Data
public class LoginUserRes implements Serializable {

    /**
     * ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 头像地址
	 */
	private String avatar;

}
