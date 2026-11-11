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

/** With Hibernate's schema generation off, the changelog still makes the whole schema. */
@SpringBootTest(properties = { "veritas.liquibase.enabled=true", "spring.jpa.hibernate.ddl-auto=none",
        "spring.datasource.url=jdbc:h2:mem:veritas-liquibase-only;DB_CLOSE_DELAY=-1" })
@ActiveProfiles("test")
class LiquibaseWithoutHibernateDdlTests {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private ProcessingProfileService profiles;

    @Test
    void theChangelogMakesTheWholeSchema() {
        assertThat(context.getBeansOfType(LiquibaseAfterHibernate.class)).hasSize(1);
        assertThat(jdbc.queryForList("SELECT exectype FROM databasechangelog", String.class)).hasSize(4)
                .containsOnly("EXECUTED");
        assertThat(profiles.exists("default")).isTrue();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM veritas_schema", Integer.class)).isZero();
    }
}
