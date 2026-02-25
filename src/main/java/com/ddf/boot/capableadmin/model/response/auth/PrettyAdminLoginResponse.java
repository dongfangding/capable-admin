package com.ddf.boot.capableadmin.model.response.auth;

import java.io.Serializable;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 20:33
 */
@Data
public class PrettyAdminLoginResponse implements Serializable {

    /**
     * token
     */
    private String accessToken;

    /**
     * 用户基本信息
     */
    private LoginUserRes user;
}
