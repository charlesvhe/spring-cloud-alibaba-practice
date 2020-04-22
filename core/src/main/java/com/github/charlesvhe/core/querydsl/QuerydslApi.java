package com.github.charlesvhe.core.querydsl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.charlesvhe.core.exception.BusinessException;
import com.github.charlesvhe.core.pojo.PageData;
import com.github.charlesvhe.core.pojo.PageRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Data
public abstract class QuerydslApi<E, I> {
    protected String currentVersion;
    protected String serviceName;
    protected Class<E> entityType;
    protected Class<I> idType;
    protected QuerydslService.API[] api;

    @Autowired
    protected RestTemplate restTemplate;
    @Autowired
    protected ObjectMapper objectMapper;

    public QuerydslApi(String currentVersion, String serviceName, Class<E> entityType, Class<I> idType, QuerydslService.API... api) {
        this.currentVersion = currentVersion;
        this.serviceName = serviceName;
        this.entityType = entityType;
        this.idType = idType;
        this.api = api;
    }

    public QuerydslApi(String currentVersion, String serviceName, Class<E> entityType, Class<I> idType) {
        this(currentVersion, serviceName, entityType, idType, QuerydslService.API.values());
    }

    protected void check(QuerydslService.API api) {
        for (QuerydslService.API a : this.api) {
            if (a.equals(api)) {
                return;
            }
        }
        throw new BusinessException(api + "接口不开放");
    }

    public I post(@RequestBody E entity) {
        check(QuerydslService.API.POST);
        ResponseEntity<I> exchange = restTemplate.exchange(
                String.format("http://%s/%s/pt/%s", serviceName, currentVersion, entityType.getSimpleName()),
                HttpMethod.POST,
                new HttpEntity<>(entity),
                ParameterizedTypeReference.forType(idType)
        );
        return exchange.getBody();
    }

    public long delete(@PathVariable I id) {
        check(QuerydslService.API.DELETE);
        return restTemplate.exchange(
                String.format("http://%s/%s/pt/%s/%s", serviceName, currentVersion, entityType.getSimpleName(), id),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Long>() {
                }
        ).getBody();
    }

    public long put(@PathVariable I id, @RequestBody E entity) {
        check(QuerydslService.API.PUT);
        return restTemplate.exchange(
                String.format("http://%s/%s/pt/%s/%s", serviceName, currentVersion, entityType.getSimpleName(), id),
                HttpMethod.PUT,
                new HttpEntity<>(entity),
                new ParameterizedTypeReference<Long>() {
                }
        ).getBody();
    }

    public PageData<E, E> search(@RequestBody PageRequest<E> pageRequest) {
        check(QuerydslService.API.SEARCH);
        ResponseEntity<PageData<E, E>> exchange = restTemplate.exchange(
                String.format("http://%s/%s/pt/%s/search", serviceName, currentVersion, entityType.getSimpleName()),
                HttpMethod.POST,
                new HttpEntity<>(pageRequest),
                ParameterizedTypeReference.forType(objectMapper.getTypeFactory().constructParametricType(PageData.class, entityType, entityType))
        );
        return exchange.getBody();
    }

//    public PageData<E, E> get(PageRequest<E> pageRequest) {
//        check(QuerydslService.API.GET);
//        return null;
//    }

    public E getOne(@PathVariable I id) {
        check(QuerydslService.API.GET_ONE);
        ResponseEntity<E> exchange = restTemplate.exchange(
                String.format("http://%s/%s/pt/%s/%s", serviceName, currentVersion, entityType.getSimpleName(), id),
                HttpMethod.GET,
                null,
                ParameterizedTypeReference.forType(entityType)
        );
        return exchange.getBody();
    }
}
