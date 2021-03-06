package cz.cas.lib.arclib.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import cz.cas.lib.arclib.store.general.DomainStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class JPAQueryFactoryProducer {
    /**
     * Produces QueryDSL {@link JPAQueryFactory} used in {@link DomainStore}.
     *
     * @param entityManager Provided JPA {@link EntityManager}
     * @return produced {@link JPAQueryFactory}
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
