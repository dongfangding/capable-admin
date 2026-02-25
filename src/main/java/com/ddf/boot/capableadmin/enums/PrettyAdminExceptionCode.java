package com.ddf.boot.capableadmin.enums;

import com.ddf.boot.common.api.exception.BaseCallbackCode;
import lombok.Getter;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 17:56
 */
@Getter
public enum PrettyAdminExceptionCode implements BaseCallbackCode {

    SYS_USER_NOT_EXISTS("SYS_USER_NOT_EXISTS", "系统用户不存在"),
    SYS_PASSWORD_NOT_MATCH("SYS_PASSWORD_NOT_MATCH", "系统用户密码不匹配"),
    MENU_TITLE_EXISTS("MENU_TITLE_EXISTS", "菜单标题已存在"),
    MENU_COMPONENT_NAME_EXISTS("MENU_COMPONENT_NAME_EXISTS", "菜单组件名称已存在"),
    MENU_FRAME_PATH_INVALID("MENU_FRAME_PATH_INVALID", "菜单外链连接不正确"),
    MENU_NOT_EXISTS("MENU_TITLE_EXISTS", "菜单不存在"),
    USER_NOT_NOT_EXISTS("USER_NOT_NOT_EXISTS", "用户不存在"),
    USER_NAME_EXISTS("USER_NAME_EXISTS", "用户名称已存在"),
    EMAIL_EXISTS("EMAIL_EXISTS", "邮箱已存在"),
    MOBILE_EXISTS("MOBILE_EXISTS", "手机号已存在"),



    ;

    /**
     * 异常code码
     */
    private final String code;

    /**
     * 异常消息
     */
    private final String description;

    /**
     * 返回给用户的异常消息
     */
    private final String bizMessage;

    PrettyAdminExceptionCode(String description) {
        this.code = null;
        this.description = description;
        this.bizMessage = description;
    }

    PrettyAdminExceptionCode(String code, String description) {
        this.code = code;
        this.description = description;
        this.bizMessage = description;
    }

    PrettyAdminExceptionCode(String code, String description, String bizMessage) {
        this.code = code;
        this.description = description;
        this.bizMessage = bizMessage;
    }
}
