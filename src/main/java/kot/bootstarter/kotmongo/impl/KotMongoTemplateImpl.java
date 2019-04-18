package kot.bootstarter.kotmongo.impl;

import kot.bootstarter.kotmongo.KotMongoTemplate;
import kot.bootstarter.kotmongo.MongoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


/**
 * @author yangyu
 */
@Service
public class KotMongoTemplateImpl implements KotMongoTemplate {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired(required = false)
    @Qualifier("secondMongoTemplate")
    private MongoTemplate secondMongoTemplate;

    @Override
    public MongoManager defaultSource() {
        return new MongoManagerImpl(mongoTemplate);
    }

    @Override
    public MongoManager secondSource() {
        return new MongoManagerImpl(secondMongoTemplate);
    }

    @Override
    public MongoManager build(MongoTemplate mongoTemplate) {
        return new MongoManagerImpl(mongoTemplate);
    }


}
