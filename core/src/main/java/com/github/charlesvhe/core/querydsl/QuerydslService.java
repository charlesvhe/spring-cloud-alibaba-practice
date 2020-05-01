package com.github.charlesvhe.core.querydsl;

import com.github.charlesvhe.core.pojo.PageData;
import com.github.charlesvhe.core.pojo.PageRequest;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public abstract class QuerydslService<E, I extends Comparable, ID extends ComparableExpressionBase<I> & Path<I>> {
    public enum API {POST, DELETE, PUT, GET, GET_ONE, SEARCH}

    public enum LIKE {STARTS, STARTS_IGNORE_CASE, ENDS, ENDS_IGNORE_CASE, CONTAINS, CONTAINS_IGNORE_CASE}

    @Value("${core.app.currentVersion:v1}")
    protected String currentVersion;
    @Value("${core.app.compatibleVersion:v1}")
    protected String[] compatibleVersion;
    @Autowired
    protected SQLQueryFactory sqlQueryFactory;
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected RelationalPathBase<E> qEntity;
    protected ID qId;
    protected List<API> enableApi;

    public QuerydslService(RelationalPathBase<E> qEntity, ID qId, API... enableApi) {
        this.qEntity = qEntity;
        this.qId = qId;
        this.enableApi = Arrays.asList(enableApi);
    }

    public QuerydslService(RelationalPathBase<E> qEntity, ID qId) {
        this(qEntity, qId, API.values());
    }

    @Autowired
    public void init(RequestMappingHandlerMapping requestMappingHandlerMapping, ApplicationContext applicationContext) throws NoSuchMethodException {
        String entityName = qEntity.getType().getSimpleName();
        // registerMapping 需要通过beanName注册 否则事务不生效
        String[] beanName = applicationContext.getBeanNamesForType(this.getClass());
        if (beanName.length != 1) {
            throw new RuntimeException("注册" + entityName + "失败 bean不唯一");
        }

        if (enableApi.contains(API.POST)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s", currentVersion, entityName)).methods(RequestMethod.POST).build(),
                    beanName[0], QuerydslService.class.getMethod("post", Object.class));
        }
        if (enableApi.contains(API.DELETE)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s/{id}", currentVersion, entityName)).methods(RequestMethod.DELETE).build(),
                    beanName[0], QuerydslService.class.getMethod("delete", Comparable.class));
        }
        if (enableApi.contains(API.PUT)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s/{id}", currentVersion, entityName)).methods(RequestMethod.PUT).build(),
                    beanName[0], QuerydslService.class.getMethod("put", Comparable.class, Object.class));
        }
        if (enableApi.contains(API.GET)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s", currentVersion, entityName)).methods(RequestMethod.GET).build(),
                    beanName[0], QuerydslService.class.getMethod("get", PageRequest.class));
        }
        if (enableApi.contains(API.GET)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s/{id}", currentVersion, entityName)).methods(RequestMethod.GET).build(),
                    beanName[0], QuerydslService.class.getMethod("getOne", Comparable.class));
        }
        if (enableApi.contains(API.SEARCH)) {
            requestMappingHandlerMapping.registerMapping(
                    RequestMappingInfo.paths(format("/%s/pt/%s/search", currentVersion, entityName)).methods(RequestMethod.POST).build(),
                    beanName[0], QuerydslService.class.getMethod("search", PageRequest.class));
        }
    }

    @Transactional
    public I post(@RequestBody E entity) {
        return sqlQueryFactory.insert(qEntity).populate(entity).executeWithKey(qId);
    }

    @Transactional
    public long delete(@PathVariable I id) {
        return sqlQueryFactory.delete(qEntity).where(qId.eq(id)).execute();
    }

    @Transactional
    public long put(@PathVariable I id, @RequestBody E entity) {
        return sqlQueryFactory.update(qEntity).populate(entity).where(qId.eq(id)).execute();
    }

    public PageData<E, E> search(@RequestBody PageRequest<E> pageRequest) throws ReflectiveOperationException {
        return get(pageRequest);
    }

    public PageData<E, E> get(PageRequest<E> pageRequest) throws ReflectiveOperationException {
        Predicate[] where = buildWhere(qEntity, pageRequest.getFilter());
        if (pageRequest.needCount()) {
            pageRequest.setTotal(sqlQueryFactory.selectFrom(qEntity).where(where).fetchCount());
        }
        return new PageData(sqlQueryFactory.selectFrom(qEntity).where(where)
                .limit(pageRequest.getSize()).offset((pageRequest.getPage() - 1) * pageRequest.getSize())
                .fetch()
                , pageRequest.next());
    }

    public E getOne(@PathVariable I id) {
        return sqlQueryFactory.selectFrom(qEntity).where(qId.eq(id)).fetchOne();
    }

    public QBean<E> ignore(Path<?>... path) {
        return select(true, path);
    }

    public QBean<E> select(Path<?>... path) {
        return select(false, path);
    }

    public QBean<E> select(boolean ignore, Path<?>... path) {
        return select(ignore, qEntity, path);
    }

    public QBean<E> ignore(String... property) {
        return select(true, property);
    }

    public QBean<E> select(String... property) {
        return select(false, property);
    }

    public QBean<E> select(boolean ignore, String... property) {
        return select(ignore, qEntity, property);
    }

    /**
     * 返回查询列
     * ignore=true返回除开property的所有列
     * ignore=false返回指定的property列
     *
     * @param ignore
     * @param qEntity
     * @param property
     * @return
     */
    public static <T> QBean<T> select(boolean ignore, RelationalPathBase<T> qEntity, String... property) {
        List<Path<?>> columnList = qEntity.getColumns().stream().filter(col -> {
            for (String p : property) {
                if (col.getMetadata().getName().equals(p)) {
                    return !ignore;
                }
            }
            return ignore;
        }).collect(Collectors.toList());
        return Projections.bean(qEntity.getType(), columnList.toArray(new Expression<?>[columnList.size()]));
    }

    /**
     * 返回查询列
     * ignore=true返回除开path的所有列
     * ignore=false返回指定的path列
     *
     * @param ignore
     * @param qEntity
     * @param path
     * @return
     */
    public static <T> QBean<T> select(boolean ignore, RelationalPathBase<T> qEntity, Expression<?>... path) {
        List<Path<?>> columnList = qEntity.getColumns().stream().filter(col -> {
            for (Expression<?> p : path) {
                if (col.equals(p)) {
                    return !ignore;
                }
            }
            return ignore;
        }).collect(Collectors.toList());
        return Projections.bean(qEntity.getType(), columnList.toArray(new Expression<?>[columnList.size()]));
    }

    /**
     * 查询多表字段时传递映射关系: Pojo属性---查询列
     * Map<String, Expression<?>> bindings = new HashMap<>();
     * bindings.put("id",qAddress.id);
     * bindings.put("userId",qUser.id);
     * bindings.put("userName",qUser.name);
     * bindings.put("zipcode",qAddress.zipcode);
     *
     * List<Pojo> result = sqlQueryFactory.select(select(Pojo.class, bindings))
     *                 .from(qAddress).innerJoin(qUser).on(qAddress.id.eq(qUser.id)).fetch();
     * @param type
     * @param bindings
     * @param <T>
     * @return
     */
    public static <T> QBean<T> select(Class<? extends T> type, Map<String, ? extends Expression<?>> bindings) {
        return Projections.bean(type, bindings);
    }

    public static <T> QBean<T> select(Class<? extends T> type, Expression<?>... path) {
        return Projections.bean(type, path);
    }

    public static Predicate[] buildWhere(RelationalPathBase pathBase, Object filter) throws ReflectiveOperationException {
        return buildWhere(pathBase, filter, LIKE.CONTAINS_IGNORE_CASE);
    }

    /**
     * 根据filter产生过滤条件 多个属性条件用and连接
     * filter属性名与pathBase属性名相同时: 字符串类型添加like指定条件 数字类型添加等于条件
     *
     * @param pathBase
     * @param filter
     * @param like
     * @return
     * @throws ReflectiveOperationException
     */
    public static Predicate[] buildWhere(RelationalPathBase pathBase, Object filter, LIKE like) throws ReflectiveOperationException {
        ArrayList<Predicate> predicateList = new ArrayList<>();
        if (filter == null) {
            return predicateList.toArray(new Predicate[predicateList.size()]);
        }
        for (Object column : pathBase.getColumns()) {
            if (column instanceof StringPath) {
                StringPath path = (StringPath) column;
                String property = path.getMetadata().getName();

                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(filter.getClass(), property);
                if (null != pd) {
                    String value = (String) pd.getReadMethod().invoke(filter);
                    if (null != value) {
                        if (like.equals(LIKE.STARTS)) {
                            predicateList.add(path.startsWith(value));
                        } else if (like.equals(LIKE.STARTS_IGNORE_CASE)) {
                            predicateList.add(path.startsWithIgnoreCase(value));
                        } else if (like.equals(LIKE.ENDS)) {
                            predicateList.add(path.endsWith(value));
                        } else if (like.equals(LIKE.ENDS_IGNORE_CASE)) {
                            predicateList.add(path.endsWithIgnoreCase(value));
                        } else if (like.equals(LIKE.CONTAINS)) {
                            predicateList.add(path.contains(value));
                        } else if (like.equals(LIKE.CONTAINS_IGNORE_CASE)) {
                            predicateList.add(path.containsIgnoreCase(value));
                        }
                    }
                }
            } else if (column instanceof NumberPath) {
                NumberPath path = (NumberPath) column;
                String property = path.getMetadata().getName();

                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(filter.getClass(), property);
                if (null != pd) {
                    Object value = pd.getReadMethod().invoke(filter);
                    if (null != value) {
                        predicateList.add(path.eq(value));
                    }
                }
            }
        }

        return predicateList.toArray(new Predicate[predicateList.size()]);
    }

    public <T> List<T> queryNative(String sql, Class<T> type, Object... args) {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(type), args);
    }

    public int updateNative(String sql, Object... args) {
        return jdbcTemplate.update(sql, args);
    }
}
