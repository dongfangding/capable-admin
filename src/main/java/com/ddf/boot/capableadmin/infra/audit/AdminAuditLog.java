package com.ddf.boot.capableadmin.infra.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理后台操作审计注解。
 * 标注在需要记录操作日志的控制器写接口上。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuditLog {

    /**
     * 所属业务模块。
     *
     * @return 模块名称
     */
    String module();

    /**
     * 当前操作名称。
     *
     * @return 动作名称
     */
    String action();
}
