package kot.bootstarter.kotmongo.config;


import kot.bootstarter.kotmongo.KotMongoTemplate;
import kot.bootstarter.kotmongo.impl.KotMongoTemplateImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author YangYu
 */
@Configuration
@ConditionalOnClass(KotMongoTemplate.class)
public class KotMongoTemplateAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(KotMongoTemplate.class)
    public KotMongoTemplate kotMongoTemplate() {
        return new KotMongoTemplateImpl();
    }
}
