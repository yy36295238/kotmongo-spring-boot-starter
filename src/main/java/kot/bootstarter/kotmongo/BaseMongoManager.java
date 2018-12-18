package kot.bootstarter.kotmongo;

import org.springframework.data.domain.Sort.Direction;

import java.util.Collection;
import java.util.List;

/**
 * @author yangyu
 */
public interface BaseMongoManager {

    /**
     * 返回指定字段
     */
    MongoManager fields(String field);

    MongoManager fields(List<String> fields);

    MongoManager skip(Integer skip);

    MongoManager limit(Integer limit);

    MongoManager page(Integer skip, Integer limit);

    MongoManager orderBy(String... sortKey);

    MongoManager direction(Direction direction);

    MongoManager eq(String key, Object value);

    MongoManager neq(String key, Object value);

    MongoManager in(String key, Object[] value);

    MongoManager in(String key, Collection<?> value);

    MongoManager nin(String key, Object[] value);

    MongoManager nin(String key, Collection<?> value);

    MongoManager lt(String key, Object value);

    MongoManager gt(String key, Object value);

    MongoManager lte(String key, Object value);

    MongoManager gte(String key, Object value);

    MongoManager or(String key, Object value);

    MongoManager like(String key, Object value);

    MongoManager between(String key, Object left, Object right);
}
