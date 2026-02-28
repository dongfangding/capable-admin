package com.ddf.boot.capableadmin.enums;

import com.ddf.boot.common.api.enums.IEnum;
import java.util.Objects;
import lombok.Setter;

/**
 * <p>菜单类型枚举</p >
 *
 * @author Snowball
 * @version 1.0
 * @since 2025/01/06 11:32
 */
public enum MenuTypeEnum implements IEnum<Integer> {
    FOLDER(0, "目录"),
    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    @Setter
    private final Integer value;

    @Setter
    private final String desc;

    MenuTypeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public boolean matches(Integer value) {
        return Objects.equals(value, this.value);
    }

    @Override
    public boolean matches(IEnum<Integer> iEnum) {
        return Objects.equals(this, iEnum);
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
