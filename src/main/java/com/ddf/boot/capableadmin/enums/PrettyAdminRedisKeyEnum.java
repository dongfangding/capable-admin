package com.ddf.boot.capableadmin.enums;


import com.ddf.boot.common.api.constraint.redis.RedisKeyConstraint;
import com.ddf.boot.common.api.constraint.redis.RedisShardingRule;
import com.ddf.boot.common.api.enums.RedisKeyTypeEnum;
import java.time.Duration;

/**
 * <p>
 * Pretty-Admin Redis Key 定义枚举
 * </p>
 * 遵循项目规范,统一管理所有缓存Key
 *
 * @author Snowball
 * @version 1.0
 * @since 2026/02/09 14:23
 */
public enum PrettyAdminRedisKeyEnum implements RedisKeyConstraint {

    /**
     * 用户基本信息缓存
     * %s: 用户ID
     */
    USER_INFO("capable-admin:user:info:%s", Duration.ofMinutes(30), RedisKeyTypeEnum.HASH),

    /**
     * 用户详情缓存(包含角色、权限、部门等完整信息)
     * %s: 用户ID
     */
    USER_DETAILS("capable-admin:user:details:%s", Duration.ofMinutes(30), RedisKeyTypeEnum.STRING),

    /**
     * 用户菜单树缓存
     * %s: 用户ID
     */
    USER_MENU_TREE("capable-admin:menu:user_tree:%s", Duration.ofMinutes(30), RedisKeyTypeEnum.STRING),

    /**
     * 用户权限集合缓存
     * %s: 用户ID
     */
    USER_PERMISSIONS("capable-admin:permission:user:%s", Duration.ofMinutes(30), RedisKeyTypeEnum.SET),

    /**
     * 菜单详情缓存
     * %s: 菜单ID
     */
    MENU_INFO("capable-admin:menu:info:%s", Duration.ofHours(1), RedisKeyTypeEnum.HASH),

    /**
     * 角色详情缓存
     * %s: 角色ID
     */
    ROLE_INFO("capable-admin:role:info:%s", Duration.ofHours(1), RedisKeyTypeEnum.HASH),

    /**
     * 角色权限集合缓存
     * %s: 角色ID
     */
    ROLE_PERMISSIONS("capable-admin:permission:role:%s", Duration.ofHours(1), RedisKeyTypeEnum.SET),

    /**
     * 在线用户信息
     * %s: Token
     */
    ONLINE_USER("capable-admin:online:user:%s", Duration.ofHours(2), RedisKeyTypeEnum.HASH),

    /**
     * Token缓存
     * %s: 用户ID
     */
    USER_TOKEN("capable-admin:token:user:%s", Duration.ofHours(2), RedisKeyTypeEnum.STRING),

    /**
     * 验证码缓存
     * %s: UUID
     */
    CAPTCHA_CODE("capable-admin:captcha:%s", Duration.ofMinutes(5), RedisKeyTypeEnum.STRING),

    /**
     * 用户会话锁(用于单点登录控制)
     * %s: 用户ID
     */
    USER_SESSION_LOCK("capable-admin:session:lock:%s", Duration.ofSeconds(10), RedisKeyTypeEnum.STRING),

    ;

    /**
     * key模板,变量使用%s代替
     */
    private final String template;

    /**
     * 过期时间
     */
    private final Duration ttl;

    /**
     * key类型
     */
    private final RedisKeyTypeEnum keyType;

    /**
     * 缓存对象类型
     */
    private final Class clazz;

    /**
     * 分片规则
     */
    private final RedisShardingRule redisShardingRule;

    PrettyAdminRedisKeyEnum(String template, RedisKeyTypeEnum keyType) {
        this.template = template;
        this.ttl = Duration.ofSeconds(-1);
        this.keyType = keyType;
        this.clazz = null;
        this.redisShardingRule = null;
    }

    PrettyAdminRedisKeyEnum(String template, Duration ttl, RedisKeyTypeEnum keyType) {
        this.template = template;
        this.ttl = ttl;
        this.keyType = keyType;
        this.clazz = null;
        this.redisShardingRule = null;
    }

    PrettyAdminRedisKeyEnum(String template, Duration ttl, RedisKeyTypeEnum keyType, Class clazz) {
        this.template = template;
        this.ttl = ttl;
        this.keyType = keyType;
        this.clazz = clazz;
        this.redisShardingRule = null;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public Duration getTtl() {
        return ttl;
    }

    @Override
    public RedisKeyTypeEnum getRedisKeyType() {
        return keyType;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }

    @Override
    public <S, M> RedisShardingRule<S, M> getRedisShardingRule() {
        return redisShardingRule;
    }
}
