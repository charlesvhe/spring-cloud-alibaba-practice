package com.github.charlesvhe.support.service;

import com.github.charlesvhe.core.CoreAutoConfiguration;
import com.github.charlesvhe.core.querydsl.QuerydslApi;
import com.github.charlesvhe.support.entity.ConfigMeta;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigMetaApi extends QuerydslApi<ConfigMeta, Long> {
    public ConfigMetaApi(@Value(CoreAutoConfiguration.PLACE_HOLD_SERVICE_NAME) String serviceName) {
        super(CoreAutoConfiguration.CURRENT_VERSION, serviceName, ConfigMeta.class, Long.class);
    }
}
