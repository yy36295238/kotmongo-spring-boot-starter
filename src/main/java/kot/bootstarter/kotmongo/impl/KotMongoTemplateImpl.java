package kot.bootstarter.kotmongo.impl;

import kot.bootstarter.kotmongo.KotMongoTemplate;
import kot.bootstarter.kotmongo.MongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


/**
 * @author yangyu
 */
@Service
public class KotMongoTemplateImpl implements KotMongoTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MongoManager createMangoManager() {
        return new MongoManagerImpl(mongoTemplate);
    }

    @Override
    public MongoTemplate mongoTemplate() {
        return mongoTemplate;
    }

}
