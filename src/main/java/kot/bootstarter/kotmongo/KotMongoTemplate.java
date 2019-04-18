package kot.bootstarter.kotmongo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author yangyu
 */
public interface KotMongoTemplate {
    /**
     * 默认数据源
     */
    MongoManager defaultSource();

    /**
     * 第二数据源
     */
    MongoManager secondSource();

    /**
     * 自定义数据源
     */
    MongoManager build(MongoTemplate mongoTemplate);
}
