package bw.co.roguesystems.ai.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;

/**
 * Runs the Liquibase changelog when veritas.liquibase.enabled is true (off by default), and only
 * once Hibernate has initialised: Hibernate makes the tables from the entities first
 * (spring.jpa.hibernate.ddl-auto), then the change sets for what is already there are marked as
 * run and the others are applied. It runs before the application takes jobs or requests.
 * <p>
 * Spring Boot's own Liquibase startup (spring.liquibase.enabled) stays off: it runs before
 * Hibernate. The runner is deliberately not a SpringLiquibase bean, which Spring Boot would also
 * order before Hibernate.
 */
@Component
@ConditionalOnProperty(name = "veritas.liquibase.enabled", havingValue = "true")
public class LiquibaseAfterHibernate implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(LiquibaseAfterHibernate.class);

    private final DataSource dataSource;
    private final ResourceLoader resourceLoader;
    private final String changeLog;

    /**
     * @param entityManagerFactory asked for so that Hibernate is initialised first
     */
    public LiquibaseAfterHibernate(DataSource dataSource, EntityManagerFactory entityManagerFactory,
            ResourceLoader resourceLoader,
            @Value("${veritas.liquibase.change-log:classpath:db/changelog/veritas-changelog.xml}") String changeLog) {
        this.dataSource = dataSource;
        this.resourceLoader = resourceLoader;
        this.changeLog = changeLog;
    }

    /** Every singleton exists by now, the entity manager factory among them. */
    @Override
    public void afterSingletonsInstantiated() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setResourceLoader(resourceLoader);
        liquibase.setChangeLog(changeLog);
        try {
            liquibase.afterPropertiesSet();
        } catch (LiquibaseException e) {
            throw new IllegalStateException("Liquibase failed on " + changeLog, e);
        }
        log.info("Liquibase ran {} after Hibernate", changeLog);
    }
}
