package com.ddf.boot.capableadmin.infra.config;

import com.ddf.boot.common.core.helper.ThreadBuilderHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2026/03/23 23:39
 */
@Configuration
public class CapableAdminThreadConfig {

	@Bean
	public ThreadPoolTaskExecutor globalExecutor() {
		return ThreadBuilderHelper.buildThreadExecutor("CapableAdminGlobalExecutor", 360, 100, 5, 5);
	}


}
