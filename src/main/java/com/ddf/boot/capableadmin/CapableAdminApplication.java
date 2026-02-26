package com.ddf.boot.capableadmin;

import com.ddf.boot.common.limit.ratelimit.annotation.EnableRateLimit;
import com.ddf.boot.common.limit.repeatable.annotation.EnableRepeatable;
import com.ddf.boot.common.limit.repeatable.validator.RedisRepeatableValidator;
import com.ddf.boot.common.mvc.logaccess.EnableLogAspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>description</p >
 *
 * @author Snowball
 * @version 1.0
 * @date 2025/01/03 16:24
 */
@SpringBootApplication
@EnableLogAspect
@EnableTransactionManagement
@MapperScan("com.ddf.boot.capableadmin.infra.mapper")
@EnableAsync
@EnableScheduling
@EnableRateLimit()
@EnableRepeatable(globalValidator = RedisRepeatableValidator.BEAN_NAME)
public class CapableAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapableAdminApplication.class, args);
    }
}
