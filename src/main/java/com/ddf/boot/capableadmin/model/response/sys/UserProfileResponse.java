package com.ddf.boot.capableadmin.model.response.sys;

import lombok.Data;

/**
 * 当前用户个人资料响应。
 */
@Data
public class UserProfileResponse {

    /**
     * 用户ID。
     */
    private Long userId;

    /**
     * 用户名。
     */
    private String username;

    /**
     * 昵称。
     */
    private String nickname;

    /**
     * 邮箱。
     */
    private String email;

    /**
     * 手机号。
     */
    private String mobile;

    /**
     * 头像地址。
     */
    private String avatar;

    /**
     * 性别。
     */
    private Integer sex;
}
