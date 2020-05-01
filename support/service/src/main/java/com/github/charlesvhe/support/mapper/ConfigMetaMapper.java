package com.github.charlesvhe.support.mapper;

import com.github.charlesvhe.support.entity.ConfigMeta;

import java.util.List;

public interface ConfigMetaMapper {
    List<ConfigMeta> findAll();
    long save(ConfigMeta configMeta);
    long update(ConfigMeta configMeta);
}
