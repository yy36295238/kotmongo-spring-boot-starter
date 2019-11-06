package kot.bootstarter.kotmongo.impl;

import kot.bootstarter.kotmongo.MongoManager;
import kot.bootstarter.kotmongo.util.ReflectionUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author yangyu
 */


@SuppressWarnings({"unchecked"})
public class MongoManagerImpl implements MongoManager {

    private static final Logger logger = LoggerFactory.getLogger(MongoManagerImpl.class);

    private static final String EXAMPLE_NOT_NULL = "example must not be null!";
    private static final String QUERY_NOT_NULL = "query condition must not be null!";
    private static final String ID = "_id";

    private MongoTemplate mongoTemplate;

    public MongoManagerImpl(MongoTemplate mongoTemplate) {
        super();
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 各种条件集合
     */
    private Map<String, Object> eqMap = null;
    private Map<String, Object> neqMap = null;
    private Map<String, Collection<?>> inMap = null;
    private Map<String, Collection<?>> ninMap = null;
    private Map<String, Object> ltMap = null;
    private Map<String, Object> gtMap = null;
    private Map<String, Object> lteMap = null;
    private Map<String, Object> gteMap = null;
    private Map<String, Object> orMap = null;
    private Map<String, Object> likeMap = null;

    private Query query = new Query();
    private Update update = new Update();
    private boolean showSql = false;

    /**
     * 创建对象
     */
    @Override
    public <T> void save(T example) {
        mongoTemplate.save(example);
    }

    @Override
    public void save(Object obj, String collection) {
        mongoTemplate.save(obj, collection);
    }

    @Override
    public <T> void insert(T example) {
        mongoTemplate.insert(example);
    }

    @Override
    public void insert(Object obj, String collection) {
        mongoTemplate.insert(obj, collection);
    }

    @Override
    public <T> void update(T example) {
        this.query();
        Assert.notNull(query.getQueryObject().size() <= 0 ? null : "", QUERY_NOT_NULL);
        mongoTemplate.updateMulti(query, getUpdateByBean(example), example.getClass());
    }

    @Override
    public void update(Map<String, Object> map, String collection) {
        this.query();
        Assert.notNull(query.getQueryObject().size() <= 0 ? null : "", QUERY_NOT_NULL);
        map.forEach((k, v) -> update.set(k, v));
        mongoTemplate.updateMulti(query, update, collection);
    }

    @Override
    public <T> void delete(T example) {
        this.query(example);
        Assert.notNull(query.getQueryObject().size() <= 0 ? null : "", QUERY_NOT_NULL);
        Assert.notNull(example, EXAMPLE_NOT_NULL);
        mongoTemplate.remove(query, example.getClass());
    }

    @Override
    public void delete(String collection) {
        this.query();
        Assert.notNull(query.getQueryObject().size() <= 0 ? null : "", QUERY_NOT_NULL);
        mongoTemplate.remove(query, collection);
    }

    @Override
    public void dropCollection(String collection) {
        mongoTemplate.dropCollection(collection);
    }

    @Override
    public <T> void dropCollection(T example) {
        mongoTemplate.dropCollection(example.getClass());
    }

    /**
     * 查询对象
     */
    @Override
    public <T> T findOne(T example) {
        Assert.notNull(example, EXAMPLE_NOT_NULL);
        return (T) mongoTemplate.findOne(query(example), example.getClass());
    }

    @Override
    public Map findOne(String collection) {
        return mongoTemplate.findOne(query(), Map.class, collection);
    }

    /**
     * 查询集合
     */
    @Override
    public <T> List<T> findPage(T example) {
        return list(example);
    }

    @Override
    public <T> List<T> findPage(T example, Integer skip, Integer limit) {
        return this.page(skip, limit).findPage(example);
    }

    @Override
    public List<Map> listMap(String collection) {
        return mongoTemplate.find(query(), Map.class, collection);
    }


    @Override
    public <T> List<T> list(T example) {
        Assert.notNull(example, EXAMPLE_NOT_NULL);
        this.query(example);
        // 分页
        if (skip != null) {
            query.skip(skip);
        }
        if (limit != null) {
            query.limit(limit);
        }
        return (List<T>) mongoTemplate.find(query, example.getClass());
    }

    /**
     * 查询数量
     */
    @Override
    public <T> long count(T example) {
        Assert.notNull(example, EXAMPLE_NOT_NULL);
        return mongoTemplate.count(query(example), example.getClass());
    }

    /**
     * 查询数量
     */
    @Override
    public long count(String collection) {
        return mongoTemplate.count(query(), collection);
    }

    @Override
    public MongoManager showSql() {
        if (showSql) {
            logger.warn("mongo sql:{} ", query);
        }
        showSql = true;
        return this;
    }

    @Override
    public MongoTemplate get() {
        return this.mongoTemplate;
    }

    private Query query() {
        return query(null);
    }

    private Query query(Object obj) {
        this.fieldsQuery().getQueryByConditionMap().sort();
        if (obj != null) {
            getQueryByBean(obj);
        }
        if (showSql) {
            showSql();
        }
        return query;
    }

    private void sort() {
        if (sortKey != null && direction != null) {
            query.with(Sort.by(direction, sortKey));
        }
    }

    /**
     * 根据bean生成query
     */
    private void getQueryByBean(Object obj) {
        ReflectionUtils.beanToMap(obj).forEach((k, v) -> {
            if (v != null) {
                query.addCriteria(Criteria.where(k).is(v));
            }
        });
    }

    /**
     * 根据bean生成query
     */
    private Update getUpdateByBean(Object obj) {
        ReflectionUtils.beanToMap(obj).forEach((k, v) -> {
            if (v != null) {
                update.set(k, v);
            }
        });
        return update;
    }

    /**
     * 返回指定字段
     */
    private MongoManagerImpl fieldsQuery() {
        if (fieldList == null) {
            return this;
        }
        final Document fieldsDocument = new Document();
        for (String field : fieldList) {
            fieldsDocument.put(field, true);
        }
        query = new BasicQuery(new Document(), fieldsDocument);
        return this;
    }

    /**
     * 封装Query
     */
    private MongoManagerImpl getQueryByConditionMap() {
        if (!CollectionUtils.isEmpty(eqMap)) {
            eqMap.forEach((k, v) -> query.addCriteria(Criteria.where(k).is(eqMap.get(k))));
        }
        if (!CollectionUtils.isEmpty(neqMap)) {
            neqMap.forEach((k, v) -> query.addCriteria(Criteria.where(k).ne(neqMap.get(k))));
        }
        if (!CollectionUtils.isEmpty(inMap)) {
            inMap.forEach((k, v) -> query.addCriteria(Criteria.where(k).in(inMap.get(k))));
        }
        if (!CollectionUtils.isEmpty(ninMap)) {
            ninMap.forEach((k, v) -> query.addCriteria(Criteria.where(k).nin(ninMap.get(k))));
        }
        if (!CollectionUtils.isEmpty(ltMap)) {
            ltMap.forEach((k, v) -> {
                Criteria criteria = Criteria.where(k).lt(ltMap.get(k));
                criteria = !CollectionUtils.isEmpty(gtMap) && gtMap.containsKey(k) ? criteria.gt(gtMap.get(k)) : criteria;
                criteria = !CollectionUtils.isEmpty(gteMap) && gteMap.containsKey(k) ? criteria.gte(gteMap.get(k)) : criteria;
                query.addCriteria(criteria);
            });
        }
        if (!CollectionUtils.isEmpty(lteMap)) {
            lteMap.forEach((k, v) -> {
                Criteria criteria = Criteria.where(k).lte(lteMap.get(k));
                criteria = !CollectionUtils.isEmpty(gtMap) && gtMap.containsKey(k) ? criteria.gt(gtMap.get(k)) : criteria;
                criteria = !CollectionUtils.isEmpty(gteMap) && gteMap.containsKey(k) ? criteria.gte(gteMap.get(k)) : criteria;
                query.addCriteria(criteria);
            });
        }
        if (!CollectionUtils.isEmpty(gtMap)) {
            gtMap.forEach((k, v) -> {
                if ((CollectionUtils.isEmpty(ltMap) || !ltMap.containsKey(k)) && (CollectionUtils.isEmpty(lteMap) || !lteMap.containsKey(k))) {
                    query.addCriteria(Criteria.where(k).gt(gtMap.get(k)));
                }
            });
        }
        if (!CollectionUtils.isEmpty(gteMap)) {
            gteMap.forEach((k, v) -> {
                if ((CollectionUtils.isEmpty(ltMap) || !ltMap.containsKey(k)) && (CollectionUtils.isEmpty(lteMap) || !lteMap.containsKey(k))) {
                    query.addCriteria(Criteria.where(k).gte(gteMap.get(k)));
                }
            });
        }
        if (!CollectionUtils.isEmpty(orMap)) {
            List<Criteria> criteriaList = new ArrayList<>();
            orMap.forEach((k, v) -> criteriaList.add(Criteria.where(k).is(orMap.get(k))));
            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
        }
        if (!CollectionUtils.isEmpty(likeMap)) {
            likeMap.forEach((k, v) -> query.addCriteria(Criteria.where(k).regex(likeMap.get(k).toString())));
        }
        return this;
    }

    /**
     * 分页、排序
     */
    private Integer skip;
    private Integer limit;
    private String[] sortKey;
    private Sort.Direction direction;

    @Override
    public MongoManager skip(Integer skip) {
        this.skip = skip == null ? 0 : (skip - 1) * limit;
        return this;
    }

    @Override
    public MongoManager limit(Integer limit) {
        this.limit = limit == null ? 30 : limit;
        return this;
    }

    @Override
    public MongoManager page(Integer skip, Integer limit) {
        this.skip(skip).limit(limit);
        return this;
    }

    @Override
    public MongoManager orderBy(String... sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    @Override
    public MongoManager direction(Sort.Direction direction) {
        this.direction = direction;
        return this;
    }

    /**
     * 条件操作
     */

    @Override
    public MongoManager eq(String key, Object value) {
        (eqMap = newMap(eqMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager neq(String key, Object value) {
        (neqMap = newMap(neqMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager in(String key, Object value) {
        return in(key, Arrays.asList(value));
    }

    @Override
    public MongoManager in(String key, Object[] value) {
        in(key, Arrays.asList(value));
        return this;
    }

    @Override
    public MongoManager in(String key, Collection<?> value) {
        (inMap = (inMap == null ? new HashMap<>() : inMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager nin(String key, Object value) {
        return nin(key, Collections.singletonList(value));
    }

    @Override
    public MongoManager nin(String key, Object[] value) {
        nin(key, Arrays.asList(value));
        return this;
    }

    @Override
    public MongoManager nin(String key, Collection<?> value) {
        (ninMap = (ninMap == null ? new HashMap<>() : ninMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager lt(String key, Object value) {
        (ltMap = newMap(ltMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager gt(String key, Object value) {
        (gtMap = newMap(gtMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager lte(String key, Object value) {
        (lteMap = newMap(lteMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager gte(String key, Object value) {
        (gteMap = newMap(gteMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager or(String key, Object value) {
        (orMap = newMap(orMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager like(String key, Object value) {
        (likeMap = newMap(likeMap)).put(key, value);
        return this;
    }

    @Override
    public MongoManager between(String key, Object left, Object right) {
        (gteMap = newMap(gteMap)).put(key, left);
        (lteMap = newMap(lteMap)).put(key, right);
        return this;
    }

    @Override
    public MongoManager orderByIdDesc() {
        this.orderBy(ID).direction(Sort.Direction.DESC);
        return this;
    }

    @Override
    public MongoManager orderByIdAsc() {
        this.orderBy(ID).direction(Sort.Direction.ASC);
        return this;
    }

    /**
     * 查询指定对象
     */
    private List<String> fieldList = null;

    @Override
    public MongoManager fields(String field) {
        fieldList = fieldList == null ? new ArrayList<>() : fieldList;
        fieldList.add(field);
        return this;
    }

    @Override
    public MongoManager fields(String... fields) {
        fieldList = fieldList == null ? new ArrayList<>() : fieldList;
        if (fields != null) {
            Collections.addAll(fieldList, fields);
        }
        return this;
    }

    @Override
    public MongoManager fields(List<String> fields) {
        fieldList = fieldList == null ? new ArrayList<>() : fieldList;
        fieldList.addAll(fields);
        return this;
    }

    private Map<String, Object> newMap(Map map) {
        return map == null ? new HashMap<>() : map;
    }

}
