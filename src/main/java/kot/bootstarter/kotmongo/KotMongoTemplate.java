package kot.bootstarter.kotmongo;

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
}
