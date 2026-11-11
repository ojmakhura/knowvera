package bw.co.roguesystems.ai;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import bw.co.roguesystems.ai.config.LiquibaseAfterHibernate;
import bw.co.roguesystems.ai.profile.ProcessingProfileService;

/** Switched on, Liquibase runs after Hibernate and marks what Hibernate made as run. */
@SpringBootTest(properties = { "veritas.liquibase.enabled=true",
        "spring.datasource.url=jdbc:h2:mem:veritas-after-hibernate;DB_CLOSE_DELAY=-1" })
@ActiveProfiles("test")
class LiquibaseAfterHibernateTests {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ProcessingProfileService profiles;

    @Test
    void theChangeSetsForWhatHibernateMadeAreMarkedAsRun() {
        assertThat(context.getBeansOfType(LiquibaseAfterHibernate.class)).hasSize(1);
        assertThat(jdbc.queryForList("SELECT exectype FROM databasechangelog", String.class)).hasSize(4)
                .containsOnly("MARK_RAN");
        assertThat(profiles.exists("default")).isTrue();
    }
}
