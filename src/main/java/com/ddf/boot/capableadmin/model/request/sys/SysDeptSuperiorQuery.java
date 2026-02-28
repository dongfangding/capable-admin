package com.ddf.boot.capableadmin.model.request.sys;

import lombok.Data;

/**
 * <p>部门查询</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 16:00
 */
@Data
public class SysDeptSuperiorQuery {

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 是否排除自己及子节点， 编辑部门时使用，不让自己选到自己及子节点
     */
    private boolean excludeSelfAndSub;

}
