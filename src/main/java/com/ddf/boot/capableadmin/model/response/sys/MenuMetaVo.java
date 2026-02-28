package com.ddf.boot.capableadmin.model.response.sys;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单元数据
 *
 * @author snowball
 * @since 2025/1/6 下午8:31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MenuMetaVo implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否缓存
     */
    private Boolean noCache;
}
