package com.ddf.boot.capableadmin;

import com.ddf.boot.common.mvc.logaccess.EnableLogAspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@MapperScan("com.ddf.boot.capableadmin.mapper")
public class PrettyAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrettyAdminApplication.class, args);
    }
}
