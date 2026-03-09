package com.ddf.boot.capableadmin.model.request.sys;

import com.ddf.boot.common.api.model.common.request.PageRequest;
import lombok.Data;

@Data
public class SysUserListRequest implements PageRequest {

    private Integer pageNum;

    private Integer pageSize;

    private String username;

    private String nickname;

    private String mobile;

    private Boolean enabled;

    private Long deptId;
}
