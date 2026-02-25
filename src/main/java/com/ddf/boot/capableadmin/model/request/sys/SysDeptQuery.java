package com.ddf.boot.capableadmin.model.request.sys;

import lombok.Data;

/**
 * <p>部门查询</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/04 16:00
 */
@Data
public class SysDeptQuery {

    /**
     * 名称
     */
    private String name;

    /**
     * 上级部门，默认为0，代表一级节点
     */
    private Long pid = 0L;

    /**
     * 状态, 0 禁用 1 启用
     */
    private Boolean enabled;

    /**
     * 是否排除自己及子节点， 编辑部门时使用，不让自己选到自己及子节点
     */
    private boolean excludeSelfAndSub;

}
