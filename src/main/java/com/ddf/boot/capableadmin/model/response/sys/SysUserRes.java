package com.ddf.boot.capableadmin.model.response.sys;

import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class SysUserRes {

    private Long userId;

    private String username;

    private String nickname;

    private Integer sex;

    private String mobile;

    private String email;

    private String avatar;

    private Boolean enabled;

    private String createBy;

    private String updateBy;

    private Date pwdResetTime;

    private Date createTime;

    private Date updateTime;

    private Set<Long> roleIds;

    private Set<Long> deptIds;
}
