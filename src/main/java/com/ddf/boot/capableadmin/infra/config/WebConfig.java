package com.ddf.boot.capableadmin.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 1. 针对 index.html 设置不缓存
//        registry.addResourceHandler("/rongcloud-im-demo.html")
//                .addResourceLocations("classpath:/static/")
//                .setCacheControl(CacheControl.noStore().mustRevalidate());

        // 2. 针对 JS/CSS 静态资源设置长期缓存（如 365 天），利用文件名 Hash 刷新
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}
