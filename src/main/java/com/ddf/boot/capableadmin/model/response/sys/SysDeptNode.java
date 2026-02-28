package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.constraint.collect.ITreeTagCollection;
import com.ddf.boot.common.api.util.DateUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/04 16:08
 */
@Data
public class SysDeptNode implements Serializable, ITreeTagCollection<Long, SysDeptNode> {
    /**
     * ID
     */
    private Long deptId;

    /**
     * 上级部门，默认为0，代表一级节点
     */
    private Long pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态, 0 禁用 1 启用
     */
    private Boolean enabled;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 创建日期
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 子节点数量
     */
    private Integer subCount;

    /**
     * 子节点
     */
    private List<SysDeptNode> children = new ArrayList<>();


    public String getFormatCreateTime() {
        return DateUtils.standardFormatSeconds(createTime);
    }

    public String getFormatUpdateTime() {
        return DateUtils.standardFormatSeconds(updateTime);
    }

    public Boolean getHasChildren() {
        return subCount > 0;
    }

    public Boolean getLeaf() {
        return subCount <= 0;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public Long getTreeId() {
        return deptId;
    }

    @Override
    public Long getTreeParentId() {
        return pid;
    }

    @Override
    public boolean isRoot() {
        return Objects.equals(0L, pid);
    }
}
