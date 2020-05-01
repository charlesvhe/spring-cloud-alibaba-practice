package com.github.charlesvhe.support.service;

import com.github.charlesvhe.core.querydsl.QuerydslService;
import com.github.charlesvhe.support.entity.ConfigMeta;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.charlesvhe.support.entity.QConfigItem.qConfigItem;
import static com.github.charlesvhe.support.entity.QConfigMeta.qConfigMeta;

@RestController
public class ConfigMetaService extends QuerydslService<ConfigMeta, Long, NumberPath<Long>> {
    public ConfigMetaService() {
        super(qConfigMeta, qConfigMeta.id);
    }

    @GetMapping("/test")
    public List<ConfigMeta> test() {
        return sqlQueryFactory
                .select(this.ignore("appId", "code"))
                .from(qConfigMeta)
                .fetch();
    }

    public Map<String, String> buildPropertyColumnMap(String appId, String code) {
        HashMap<String, String> resultMap = new HashMap<>();
        List<ConfigMeta> resultList = sqlQueryFactory
                .selectFrom(qConfigMeta)
                .where(qConfigMeta.appId.eq(appId).and(qConfigMeta.code.eq(code))).fetch();
        for (ConfigMeta configMeta : resultList) {
            resultMap.put(configMeta.getProperty(), configMeta.getColumnName());
        }
        // 通用默认映射
        resultMap.put(qConfigItem.id.getMetadata().getName(), qConfigItem.id.getMetadata().getName());
        resultMap.put(qConfigItem.parentId.getMetadata().getName(), qConfigItem.parentId.getMetadata().getName());
        resultMap.put(qConfigItem.sort.getMetadata().getName(), qConfigItem.sort.getMetadata().getName());
        resultMap.put(qConfigItem.gmtCreate.getMetadata().getName(), qConfigItem.gmtCreate.getMetadata().getName());
        resultMap.put(qConfigItem.gmtModified.getMetadata().getName(), qConfigItem.gmtModified.getMetadata().getName());

        return resultMap;
    }
}
