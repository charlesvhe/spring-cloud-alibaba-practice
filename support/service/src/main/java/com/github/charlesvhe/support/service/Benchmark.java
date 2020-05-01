package com.github.charlesvhe.support.service;

import com.github.charlesvhe.support.entity.ConfigMeta;
import com.github.charlesvhe.support.mapper.ConfigMetaMapper;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.charlesvhe.support.entity.QConfigMeta.qConfigMeta;

@Service
public class Benchmark {
    @Autowired
    private ConfigMetaMapper configMetaMapper;
    @Autowired
    protected SQLQueryFactory sqlQueryFactory;

    public List<ConfigMeta> testMybatisQuery() {
        return configMetaMapper.findAll();
    }

    @Transactional
    public long testMybatisSave(ConfigMeta configMeta) {
        return configMetaMapper.save(configMeta);
    }

    @Transactional
    public long testMybatisUpdate(ConfigMeta configMeta) {
        return configMetaMapper.update(configMeta);
    }

    public List<ConfigMeta> testQuerydslQuery() {
        return sqlQueryFactory.selectFrom(qConfigMeta).fetch();
    }

    @Transactional
    public long testQuerydslSave(ConfigMeta configMeta) {
        return sqlQueryFactory.insert(qConfigMeta).populate(configMeta).execute();
    }

    @Transactional
    public long testQuerydslUpdate(ConfigMeta configMeta) {
        return sqlQueryFactory.update(qConfigMeta).populate(configMeta).execute();
    }
}
