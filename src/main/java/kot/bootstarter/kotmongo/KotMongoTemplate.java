package kot.bootstarter.kotmongo;

import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author yangyu
 */
public interface KotMongoTemplate {
	MongoManager createMangoManager();

	MongoTemplate mongoTemplate();
}
