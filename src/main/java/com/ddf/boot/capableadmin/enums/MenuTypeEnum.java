package com.ddf.boot.capableadmin.enums;

import com.ddf.boot.common.api.enums.IEnum;
import java.util.Objects;

/**
 * <p>菜单类型枚举</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/06 11:32
 */
public enum MenuTypeEnum implements IEnum<String> {
    CATALOG("catalog", "目录"),
    MENU("menu", "菜单"),
    BUTTON("button", "按钮"),
	EMBEDDED("embedded", "内嵌"),
	LINK("link", "外链"),

		;

    private final String value;

    private final String desc;

    MenuTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public boolean matches(String value) {
        return Objects.equals(value, this.value);
    }

    @Override
    public boolean matches(IEnum<String> iEnum) {
        return Objects.equals(this, iEnum);
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String getValue() {
        return value;
    }
}
