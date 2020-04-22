package com.github.charlesvhe.core;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ComponentScan("com.github.charlesvhe.core")
public class CoreAutoConfiguration {
    // core.service.support-service=support-service-hy来调用support-service-hy服务 方便本地联调
    public static final String SERVICE_NAME = "support-service";
    public static final String PLACE_HOLD_SERVICE_NAME = "${core.service." + SERVICE_NAME + ":" + SERVICE_NAME + "}";

    public static final String CURRENT_VERSION = "v1";
    public static final String COMPATIBLE_VERSION = "v1";
}
