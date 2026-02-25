/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ddf.boot.capableadmin.model.response.sys;

import com.ddf.boot.common.api.constraint.collect.ITreeTagCollection;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 构建菜单路由节点（左侧菜单栏）
 *
 * @author snowball
 * @date 2025/1/6 下午8:28
 **/
@Data
public class BuildMenuRouteNode implements Serializable, ITreeTagCollection<Long, BuildMenuRouteNode> {
    /**
     * ID
     */
    private Long menuId;

    /**
     * 上级菜单ID，默认0为一级节点
     */
    private Long pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 跳转路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * 菜单元数据
     */
    private MenuMetaVo meta;

    /**
     * 子节点
     */
    private List<BuildMenuRouteNode> children;

    @Override
    public Long getTreeId() {
        return menuId;
    }

    @Override
    public Long getTreeParentId() {
        return pid;
    }
}
