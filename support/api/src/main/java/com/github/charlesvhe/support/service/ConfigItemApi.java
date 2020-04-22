package com.github.charlesvhe.support.service;

import com.github.charlesvhe.core.CoreAutoConfiguration;
import com.github.charlesvhe.core.querydsl.QuerydslApi;
import com.github.charlesvhe.support.entity.ConfigItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigItemApi extends QuerydslApi<ConfigItem, Long> {
    public ConfigItemApi(@Value(CoreAutoConfiguration.PLACE_HOLD_SERVICE_NAME) String serviceName) {
        super(CoreAutoConfiguration.CURRENT_VERSION, serviceName, ConfigItem.class, Long.class);
    }
}
