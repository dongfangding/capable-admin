package com.ddf.boot.capableadmin.config;

import com.ddf.boot.common.core.helper.ThreadBuilderHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>dispatch服务线程池配置</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2024/12/30 20:29
 */
@Configuration
public class PrettyAdminThreadConfig {

    /**
     * 用户登录事件处理线程池
     *
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor userLoginHistoryExecutor() {
        return ThreadBuilderHelper.buildThreadExecutor("user-login-history-executor-", 400, 1000);
    }
}
