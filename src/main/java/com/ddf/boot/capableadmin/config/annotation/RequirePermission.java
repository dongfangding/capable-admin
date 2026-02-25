package com.ddf.boot.capableadmin.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 权限校验注解
 * </p>
 * 用于Controller方法上,控制访问权限
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/02/09 15:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    /**
     * 权限标识
     * 支持多个权限,默认只需满足其中一个(OR)
     */
    String[] value();

    /**
     * 逻辑操作
     * AND: 必须拥有所有权限
     * OR: 只需拥有其中一个权限
     */
    Logical logical() default Logical.OR;

    enum Logical {
        AND, OR
    }
}
