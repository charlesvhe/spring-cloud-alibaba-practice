package com.github.charlesvhe.support.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.charlesvhe.core.pojo.PageData;
import com.github.charlesvhe.core.pojo.PageRequest;
import com.github.charlesvhe.core.querydsl.QuerydslService;
import com.github.charlesvhe.support.entity.ConfigItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.charlesvhe.support.entity.QConfigItem.qConfigItem;


@RestController
@Transactional
public class ConfigItemService extends QuerydslService<ConfigItem, Long, NumberPath<Long>> {
    @Autowired
    private ConfigMetaService configMetaService;
    @Autowired
    private ObjectMapper objectMapper;

    public ConfigItemService() {
        super(qConfigItem, qConfigItem.id);
    }

    @PostMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}")
    public long convertPost(@PathVariable("appId") String appId,
                            @PathVariable("metaCode") String metaCode,
                            @RequestBody ObjectNode objectNode) {
        ConfigItem configItem = convert(configMetaService.buildPropertyColumnMap(appId, metaCode), objectNode);
        // 校验 appId metaCode id
        configItem.setAppId(appId);
        configItem.setMetaCode(metaCode);
        return sqlQueryFactory.insert(qEntity).populate(configItem).executeWithKey(Long.class);
    }

    @DeleteMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}/{id:\\d+}")
    public long convertDelete(@PathVariable("appId") String appId,
                              @PathVariable("metaCode") String metaCode,
                              @PathVariable("id") Long id) {
        // 校验 appId metaCode id
        return sqlQueryFactory.delete(qConfigItem)
                .where(qConfigItem.id.eq(id)
                        .and(qConfigItem.appId.eq(appId))
                        .and(qConfigItem.metaCode.eq(metaCode)))
                .execute();
    }

    @PutMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}/{id:\\d+}")
    public long convertPut(@PathVariable("appId") String appId,
                           @PathVariable("metaCode") String metaCode,
                           @PathVariable("id") Long id,
                           @RequestBody ObjectNode objectNode) {
        ConfigItem configItem = convert(configMetaService.buildPropertyColumnMap(appId, metaCode), objectNode);
        // 校验 appId metaCode id
        return sqlQueryFactory.update(qEntity).populate(configItem)
                .where(qId.eq(id)
                        .and(qConfigItem.appId.eq(appId))
                        .and(qConfigItem.metaCode.eq(metaCode)))
                .execute();
    }

    @PostMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}/search")
    public PageData<ObjectNode, ObjectNode> convertSearch(@PathVariable("appId") String appId,
                                                          @PathVariable("metaCode") String metaCode,
                                                          @RequestBody PageRequest<ObjectNode> pageRequest) throws ReflectiveOperationException {
        return convertGet(appId, metaCode, pageRequest);
    }

    @GetMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}")
    public PageData<ObjectNode, ObjectNode> convertGet(@PathVariable("appId") String appId,
                                                       @PathVariable("metaCode") String metaCode,
                                                       @RequestBody PageRequest<ObjectNode> pageRequest) throws ReflectiveOperationException {
        Map<String, String> propertyColumnMap = configMetaService.buildPropertyColumnMap(appId, metaCode);
        // 校验 appId metaCode id
        ConfigItem configItem = convert(propertyColumnMap, pageRequest.getFilter());
        configItem.setAppId(appId);
        configItem.setMetaCode(metaCode);

        Predicate[] where = buildWhere(qEntity, configItem);
        if (pageRequest.needCount()) {
            pageRequest.setTotal(sqlQueryFactory.selectFrom(qEntity).where(where).fetchCount());
        }

        List<ConfigItem> configItemList = sqlQueryFactory.selectFrom(qEntity).where(where)
                .limit(pageRequest.getSize()).offset((pageRequest.getPage() - 1) * pageRequest.getSize())
                .fetch();
        List<ObjectNode> objectNodeList = configItemList.stream().map(c -> convert(propertyColumnMap, c)).collect(Collectors.toList());

        return new PageData(objectNodeList, pageRequest.next());
    }

    @GetMapping("/{version}/pb/ConfigItem/{appId}/{metaCode}/{id:\\d+}")
    public ObjectNode convertGetOne(@PathVariable("appId") String appId,
                                    @PathVariable("metaCode") String metaCode,
                                    @PathVariable("id") Long id) {
        // 校验 appId metaCode id
        ConfigItem configItem = sqlQueryFactory.selectFrom(qEntity)
                .where(qId.eq(id)
                        .and(qConfigItem.appId.eq(appId))
                        .and(qConfigItem.metaCode.eq(metaCode)))
                .fetchOne();
        return convert(configMetaService.buildPropertyColumnMap(appId, metaCode), configItem);
    }

    public ConfigItem convert(Map<String, String> propertyColumnMap, ObjectNode objectNode) {
        if (null == objectNode) {
            return new ConfigItem();
        }
        ObjectNode copy = objectNode.deepCopy();
        for (Map.Entry<String, String> propertyColumnNameEntry : propertyColumnMap.entrySet()) {
            String property = propertyColumnNameEntry.getKey();
            String column = propertyColumnNameEntry.getValue();

            JsonNode node = copy.remove(property);
            if (null != node) {
                copy.set(column, node);
            }
        }
        return objectMapper.convertValue(copy, ConfigItem.class);
    }

    public ObjectNode convert(Map<String, String> propertyColumnMap, ConfigItem configItem) {
        if (null == configItem) {
            return null;
        }
        ObjectNode configItemNode = objectMapper.convertValue(configItem, ObjectNode.class);
        ObjectNode result = objectMapper.createObjectNode();
        for (Map.Entry<String, String> propertyColumnNameEntry : propertyColumnMap.entrySet()) {
            String property = propertyColumnNameEntry.getKey();
            String column = propertyColumnNameEntry.getValue();

            JsonNode value = configItemNode.get(column);
            if (null != value) {
                result.set(property, value);
            }
        }
        return result;
    }
}
